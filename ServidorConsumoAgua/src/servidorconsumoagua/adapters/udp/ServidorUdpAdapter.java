package servidorconsumoagua.adapters.udp;

import servidorconsumoagua.domain.model.ConsumoAgua;
import servidorconsumoagua.ports.input.ProcesarConsumoAguaUseCase;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ServidorUdpAdapter {
    private final int puerto;
    private final ProcesarConsumoAguaUseCase casoDeUso;

    public ServidorUdpAdapter(int puerto, ProcesarConsumoAguaUseCase casoDeUso) {
        this.puerto = puerto;
        this.casoDeUso = casoDeUso;
    }

    public void iniciar() {
        System.out.println("Servidor UDP de consumo de agua escuchando en el puerto " + puerto);
        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            while (true) {
                byte[] datos = new byte[1024];
                DatagramPacket entrada = new DatagramPacket(datos, datos.length);
                socket.receive(entrada);
                String solicitud = new String(entrada.getData(), 0, entrada.getLength(), StandardCharsets.UTF_8);
                byte[] respuesta = procesar(solicitud).getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(respuesta, respuesta.length, entrada.getAddress(), entrada.getPort()));
            }
        } catch (IOException ex) {
            System.err.println("No se pudo iniciar el servidor UDP: " + ex.getMessage());
        }
    }

    private String procesar(String mensaje) {
        String[] partes = mensaje.trim().split(";", -1);
        if (partes.length != 2) {
            return "ERROR;Formato inválido. Use litros;habitantes";
        }
        try {
            double litros = Double.parseDouble(partes[0]);
            int habitantes = Integer.parseInt(partes[1]);
            double promedio = casoDeUso.procesar(new ConsumoAgua(litros, habitantes));
            return String.format(Locale.US, "OK;%.2f", promedio);
        } catch (NumberFormatException ex) {
            return "ERROR;Los valores deben ser numéricos.";
        } catch (IllegalArgumentException ex) {
            return "ERROR;" + ex.getMessage();
        } catch (RuntimeException ex) {
            return "ERROR;No fue posible procesar la solicitud.";
        }
    }
}
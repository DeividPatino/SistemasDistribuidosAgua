package clienteconsumoagua.adapters.udp;

import clienteconsumoagua.domain.model.ConsumoAgua;
import clienteconsumoagua.domain.model.RespuestaConsumo;
import clienteconsumoagua.ports.output.ClienteConsumoAguaPort;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class ClienteUdpAdapter implements ClienteConsumoAguaPort {
    private final String host;
    private final int puerto;
    private final int timeout;

    public ClienteUdpAdapter(String host, int puerto, int timeout) {
        this.host = host;
        this.puerto = puerto;
        this.timeout = timeout;
    }

    @Override
    public RespuestaConsumo solicitar(ConsumoAgua solicitud) {
        String mensaje = solicitud.litrosTotales() + ";" + solicitud.habitantes();
        byte[] datos = mensaje.getBytes(StandardCharsets.UTF_8);
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress direccion = InetAddress.getByName(host);
            socket.send(new DatagramPacket(datos, datos.length, direccion, puerto));
            socket.setSoTimeout(timeout);
            byte[] respuesta = new byte[1024];
            DatagramPacket paquete = new DatagramPacket(respuesta, respuesta.length);
            socket.receive(paquete);
            return interpretar(new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8));
        } catch (SocketTimeoutException ex) {
            return RespuestaConsumo.error("Tiempo de espera agotado. Verifique que el servidor esté ejecutándose.");
        } catch (IOException ex) {
            return RespuestaConsumo.error("No se pudo comunicar con el servidor.");
        }
    }

    private RespuestaConsumo interpretar(String mensaje) {
        String[] partes = mensaje.split(";", 2);
        if (partes.length != 2) {
            return RespuestaConsumo.error("Respuesta inválida del servidor.");
        }
        if ("OK".equals(partes[0])) {
            try {
                return RespuestaConsumo.exitosa(Double.parseDouble(partes[1]));
            } catch (NumberFormatException ex) {
                return RespuestaConsumo.error("El servidor devolvió un resultado inválido.");
            }
        }
        return RespuestaConsumo.error(partes[1]);
    }
}
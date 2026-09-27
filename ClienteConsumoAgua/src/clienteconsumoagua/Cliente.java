package clienteconsumoagua;

import clienteconsumoagua.adapters.gui.ClienteSwingAdapter;
import clienteconsumoagua.adapters.udp.ClienteUdpAdapter;
import clienteconsumoagua.application.SolicitarConsumoAguaService;
import clienteconsumoagua.ports.input.SolicitarConsumoAguaUseCase;
import clienteconsumoagua.ports.output.ClienteConsumoAguaPort;
import javax.swing.SwingUtilities;

public class Cliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        ClienteConsumoAguaPort udp = new ClienteUdpAdapter(HOST, PUERTO, 3000);
        SolicitarConsumoAguaUseCase casoDeUso = new SolicitarConsumoAguaService(udp);
        SwingUtilities.invokeLater(() -> new ClienteSwingAdapter(casoDeUso).mostrar());
    }
}
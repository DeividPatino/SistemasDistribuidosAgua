package servidorconsumoagua;

import servidorconsumoagua.adapters.udp.ServidorUdpAdapter;
import servidorconsumoagua.application.ProcesarConsumoAguaService;
import servidorconsumoagua.domain.service.CalculadorConsumoAgua;

public class Servidor {
    public static void main(String[] args) {
        ProcesarConsumoAguaService casoDeUso = new ProcesarConsumoAguaService(new CalculadorConsumoAgua());
        new ServidorUdpAdapter(5000, casoDeUso).iniciar();
    }
}
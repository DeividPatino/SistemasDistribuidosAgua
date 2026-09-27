package clienteconsumoagua.application;

import clienteconsumoagua.domain.model.ConsumoAgua;
import clienteconsumoagua.domain.model.RespuestaConsumo;
import clienteconsumoagua.ports.input.SolicitarConsumoAguaUseCase;
import clienteconsumoagua.ports.output.ClienteConsumoAguaPort;

public class SolicitarConsumoAguaService implements SolicitarConsumoAguaUseCase {
    private final ClienteConsumoAguaPort puertoSalida;

    public SolicitarConsumoAguaService(ClienteConsumoAguaPort puertoSalida) {
        this.puertoSalida = puertoSalida;
    }

    @Override
    public RespuestaConsumo solicitar(double litrosTotales, int habitantes) {
        return puertoSalida.solicitar(new ConsumoAgua(litrosTotales, habitantes));
    }
}
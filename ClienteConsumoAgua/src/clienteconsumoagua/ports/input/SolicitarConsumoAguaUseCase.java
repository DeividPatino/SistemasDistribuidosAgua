package clienteconsumoagua.ports.input;

import clienteconsumoagua.domain.model.RespuestaConsumo;

public interface SolicitarConsumoAguaUseCase {
    RespuestaConsumo solicitar(double litrosTotales, int habitantes);
}
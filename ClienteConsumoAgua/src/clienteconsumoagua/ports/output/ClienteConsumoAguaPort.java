package clienteconsumoagua.ports.output;

import clienteconsumoagua.domain.model.ConsumoAgua;
import clienteconsumoagua.domain.model.RespuestaConsumo;

public interface ClienteConsumoAguaPort {
    RespuestaConsumo solicitar(ConsumoAgua solicitud);
}
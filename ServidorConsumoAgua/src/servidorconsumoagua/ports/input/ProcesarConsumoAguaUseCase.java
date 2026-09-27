package servidorconsumoagua.ports.input;

import servidorconsumoagua.domain.model.ConsumoAgua;

public interface ProcesarConsumoAguaUseCase {
    double procesar(ConsumoAgua consumo);
}
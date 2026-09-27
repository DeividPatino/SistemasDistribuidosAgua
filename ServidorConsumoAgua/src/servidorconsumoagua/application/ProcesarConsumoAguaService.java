package servidorconsumoagua.application;

import servidorconsumoagua.domain.model.ConsumoAgua;
import servidorconsumoagua.domain.service.CalculadorConsumoAgua;
import servidorconsumoagua.ports.input.ProcesarConsumoAguaUseCase;

public class ProcesarConsumoAguaService implements ProcesarConsumoAguaUseCase {
    private final CalculadorConsumoAgua calculador;

    public ProcesarConsumoAguaService(CalculadorConsumoAgua calculador) {
        this.calculador = calculador;
    }

    @Override
    public double procesar(ConsumoAgua consumo) {
        return calculador.calcularPromedio(consumo);
    }
}
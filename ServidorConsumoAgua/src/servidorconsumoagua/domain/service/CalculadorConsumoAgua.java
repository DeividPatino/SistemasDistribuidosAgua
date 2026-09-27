package servidorconsumoagua.domain.service;

import servidorconsumoagua.domain.model.ConsumoAgua;

public class CalculadorConsumoAgua {
    public double calcularPromedio(ConsumoAgua consumo) {
        if (!Double.isFinite(consumo.litrosTotales()) || consumo.litrosTotales() <= 0) {
            throw new IllegalArgumentException("Los litros deben ser mayores que cero.");
        }
        if (consumo.habitantes() <= 0) {
            throw new IllegalArgumentException("Los habitantes deben ser mayores que cero.");
        }
        return consumo.litrosTotales() / consumo.habitantes();
    }
}
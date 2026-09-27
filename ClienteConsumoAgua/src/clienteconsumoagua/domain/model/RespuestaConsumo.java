package clienteconsumoagua.domain.model;

public record RespuestaConsumo(boolean exitosa, double promedio, String mensaje) {
    public static RespuestaConsumo exitosa(double promedio) {
        return new RespuestaConsumo(true, promedio, "");
    }

    public static RespuestaConsumo error(String mensaje) {
        return new RespuestaConsumo(false, 0, mensaje);
    }
}
package pe.edu.utp.tiendazapatillas.modelo.entidades;

import pe.edu.utp.tiendazapatillas.modelo.estados.ContextoVenta;
import java.math.BigDecimal;

// Un registro simple para mantener los datos del historial de ventas.
public record VentaHistorial(int id, BigDecimal total, ContextoVenta contexto) {
    public String getEstado() {
        // Extrae el nombre simple de la clase del estado actual.
        return contexto.getEstadoActual().getClass().getSimpleName();
    }
}

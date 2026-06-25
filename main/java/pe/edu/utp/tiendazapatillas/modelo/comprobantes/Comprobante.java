package pe.edu.utp.tiendazapatillas.modelo.comprobantes;

import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * [SOLID - L] Clase base para todos los tipos de comprobantes.
 * Define la estructura común y el comportamiento que debe ser implementado.
 */
public abstract class Comprobante {

    protected LocalDate fechaEmision;
    protected List<ComponenteZapatilla> items;
    protected BigDecimal total;

    public Comprobante(List<ComponenteZapatilla> items) {
        this.fechaEmision = LocalDate.now();
        this.items = items;
        this.total = calcularTotal();
    }

    protected abstract BigDecimal calcularTotal();
    public abstract String generarContenido();

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public List<ComponenteZapatilla> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}

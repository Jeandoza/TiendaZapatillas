package pe.edu.utp.tiendazapatillas.modelo.comprobantes;

import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;
import java.math.BigDecimal;
import java.util.List;

public class Boleta extends Comprobante {

    public Boleta(List<ComponenteZapatilla> items) {
        super(items);
    }

    @Override
    protected BigDecimal calcularTotal() {
        return items.stream()
                .map(ComponenteZapatilla::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== BOLETA DE VENTA =====\n");
        sb.append("Fecha: ").append(fechaEmision).append("\n");
        sb.append("-----------------------------\n");
        items.forEach(item -> sb.append(String.format("- %s: S/ %.2f\n", item.getDescripcion(), item.getPrecio())));
        sb.append("-----------------------------\n");
        sb.append(String.format("TOTAL: S/ %.2f\n", total));
        sb.append("=============================\n");
        return sb.toString();
    }
}

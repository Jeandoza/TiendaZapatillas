package pe.edu.utp.tiendazapatillas.modelo.comprobantes;

import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Factura extends Comprobante {

    private static final BigDecimal IGV_TASA = new BigDecimal("0.18");
    private BigDecimal subtotal;
    private BigDecimal igv;

    public Factura(List<ComponenteZapatilla> items) {
        super(items);
    }

    @Override
    protected BigDecimal calcularTotal() {
        this.subtotal = items.stream()
                .map(ComponenteZapatilla::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.igv = subtotal.multiply(IGV_TASA).setScale(2, RoundingMode.HALF_UP);
        return subtotal.add(igv);
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("======== FACTURA DE VENTA ========\n");
        sb.append("Fecha: ").append(fechaEmision).append("\n");
        sb.append("----------------------------------\n");
        items.forEach(item -> sb.append(String.format("- %s: S/ %.2f\n", item.getDescripcion(), item.getPrecio())));
        sb.append("----------------------------------\n");
        sb.append(String.format("SUBTOTAL: S/ %.2f\n", subtotal));
        sb.append(String.format("IGV (18%%): S/ %.2f\n", igv));
        sb.append(String.format("TOTAL: S/ %.2f\n", total));
        sb.append("==================================\n");
        return sb.toString();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }
}

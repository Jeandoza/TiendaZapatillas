package pe.edu.utp.tiendazapatillas.modelo.precios;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * [DECORATOR] Decorador Concreto.
 * Añade un descuento de liquidación al precio de la zapatilla.
 */
public class DescuentoLiquidacion extends ZapatillaDecorador {

    private final BigDecimal porcentajeDescuento;

    public DescuentoLiquidacion(ComponenteZapatilla zapatillaDecorada, BigDecimal porcentajeDescuento) {
        super(zapatillaDecorada);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public BigDecimal getPrecio() {
        BigDecimal precioOriginal = super.getPrecio();
        BigDecimal factorDescuento = BigDecimal.ONE.subtract(porcentajeDescuento.divide(new BigDecimal("100")));
        return precioOriginal.multiply(factorDescuento).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " (Liquidación)";
    }
}

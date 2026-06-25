package pe.edu.utp.tiendazapatillas.modelo.precios;

import java.math.BigDecimal;

/**
 * [DECORATOR] Decorador Concreto.
 * Añade un costo adicional por ser una edición especial.
 */
public class EdicionEspecial extends ZapatillaDecorador {

    private final BigDecimal costoAdicional;

    public EdicionEspecial(ComponenteZapatilla zapatillaDecorada, BigDecimal costoAdicional) {
        super(zapatillaDecorada);
        this.costoAdicional = costoAdicional;
    }

    @Override
    public BigDecimal getPrecio() {
        BigDecimal precioBase = super.getPrecio();
        return precioBase.add(costoAdicional);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " (Edición Especial)";
    }
}

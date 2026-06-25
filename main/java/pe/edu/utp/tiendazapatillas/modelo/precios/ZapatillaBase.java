package pe.edu.utp.tiendazapatillas.modelo.precios;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import java.math.BigDecimal;

/**
 * [DECORATOR] Componente Concreto.
 * Es la clase base que será envuelta por los decoradores.
 */
public class ZapatillaBase implements ComponenteZapatilla {

    private final Zapatilla zapatilla;

    public ZapatillaBase(Zapatilla zapatilla) {
        this.zapatilla = zapatilla;
    }

    @Override
    public Zapatilla getZapatilla() {
        return this.zapatilla;
    }

    @Override
    public BigDecimal getPrecio() {
        return this.zapatilla.getPrecio();
    }

    @Override
    public String getDescripcion() {
        return zapatilla.getMarca().getNombre() + " " + zapatilla.getModelo();
    }
}

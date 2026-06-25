package pe.edu.utp.tiendazapatillas.modelo.precios;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import java.math.BigDecimal;

/**
 * [DECORATOR] Decorador Abstracto.
 * Mantiene una referencia al objeto componente y delega las llamadas a este.
 * Permite que los decoradores concretos extiendan el comportamiento.
 */
public abstract class ZapatillaDecorador implements ComponenteZapatilla {

    protected final ComponenteZapatilla zapatillaDecorada;

    protected ZapatillaDecorador(ComponenteZapatilla zapatillaDecorada) {
        this.zapatillaDecorada = zapatillaDecorada;
    }

    @Override
    public Zapatilla getZapatilla() {
        return zapatillaDecorada.getZapatilla();
    }

    @Override
    public BigDecimal getPrecio() {
        return zapatillaDecorada.getPrecio(); // Delega la llamada
    }

    @Override
    public String getDescripcion() {
        return zapatillaDecorada.getDescripcion(); // Delega la llamada
    }
}

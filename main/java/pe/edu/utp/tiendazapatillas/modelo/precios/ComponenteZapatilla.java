package pe.edu.utp.tiendazapatillas.modelo.precios;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import java.math.BigDecimal;

/**
 * [DECORATOR] Interfaz Componente.
 * Define la operación que será alterada por los decoradores.
 */
public interface ComponenteZapatilla {
    Zapatilla getZapatilla();
    BigDecimal getPrecio();
    String getDescripcion();
}

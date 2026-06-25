package pe.edu.utp.tiendazapatillas.servicio.fabricas;

import pe.edu.utp.tiendazapatillas.modelo.comprobantes.Boleta;
import pe.edu.utp.tiendazapatillas.modelo.comprobantes.Comprobante;
import pe.edu.utp.tiendazapatillas.modelo.comprobantes.Factura;
import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;

import java.util.List;

/**
 * [FACTORY METHOD] Fábrica para la creación de objetos Comprobante.
 * Encapsula la lógica de instanciación para decidir qué tipo de comprobante crear.
 */
public class FabricaComprobante {

    public enum TipoComprobante {
        BOLETA,
        FACTURA
    }

    public static Comprobante crearComprobante(TipoComprobante tipo, List<ComponenteZapatilla> items) {
        return switch (tipo) {
            case BOLETA -> new Boleta(items);
            case FACTURA -> new Factura(items);
            default -> throw new IllegalArgumentException("Tipo de comprobante no soportado: " + tipo);
        };
    }
}

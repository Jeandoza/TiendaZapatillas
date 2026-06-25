package pe.edu.utp.tiendazapatillas.modelo.estados;

/**
 * [STATE] Estado Concreto.
 * Representa una venta que ha sido anulada. Es un estado final.
 */
public class EstadoAnulado implements EstadoVenta {

    @Override
    public void registrar(ContextoVenta contexto) {
        System.out.println("Acción no permitida: La venta está anulada.");
    }

    @Override
    public void pagar(ContextoVenta contexto) {
        System.out.println("Acción no permitida: No se puede pagar una venta anulada.");
    }

    @Override
    public void anular(ContextoVenta contexto) {
        System.out.println("La venta ya se encuentra en estado Anulado.");
    }
}

package pe.edu.utp.tiendazapatillas.modelo.estados;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;

/**
 * [STATE] Estado Concreto.
 * Representa una venta que ha sido pagada y cuyo stock ha sido descontado.
 */
public class EstadoPagado implements EstadoVenta {

    @Override
    public void registrar(ContextoVenta contexto) {
        System.out.println("Acción no permitida: La venta ya fue registrada y pagada.");
    }

    @Override
    public void pagar(ContextoVenta contexto) {
        System.out.println("Acción no permitida: La venta ya se encuentra en estado Pagado.");
    }

    @Override
    public void anular(ContextoVenta contexto) {
        // Lógica para anular una venta que ya fue pagada.
        // Esto implica devolver el stock.
        System.out.println("Anulando venta pagada y devolviendo stock...");

        Zapatilla zapatilla = contexto.getZapatilla();
        int cantidadDevuelta = contexto.getCantidad();
        int stockActual = zapatilla.getStock();
        int nuevoStock = stockActual + cantidadDevuelta;

        contexto.getRepositorioZapatilla().actualizarStock(zapatilla.getId(), nuevoStock);
        zapatilla.setStock(nuevoStock); // Actualizar el objeto en memoria

        System.out.println("Stock devuelto. Nuevo stock: " + nuevoStock);
        contexto.cambiarEstado(new EstadoAnulado());
        System.out.println("Transición de estado: Pagado -> Anulado.");
    }
}

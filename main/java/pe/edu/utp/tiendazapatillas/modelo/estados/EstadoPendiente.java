package pe.edu.utp.tiendazapatillas.modelo.estados;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;

/**
 * [STATE] Estado Concreto.
 * Representa el estado inicial de una venta.
 */
public class EstadoPendiente implements EstadoVenta {

    @Override
    public void registrar(ContextoVenta contexto) {
        // La lógica de registro ya ocurrió al crear el contexto.
        // Desde Pendiente, podemos pasar a Pagado o Anulado.
        System.out.println("Venta registrada. Estado actual: Pendiente.");
    }

    @Override
    public void pagar(ContextoVenta contexto) {
        // Lógica para procesar el pago...
        System.out.println("Procesando pago...");

        // Descontar stock
        Zapatilla zapatilla = contexto.getZapatilla();
        int cantidadVendida = contexto.getCantidad();
        int stockActual = zapatilla.getStock();

        if (stockActual >= cantidadVendida) {
            int nuevoStock = stockActual - cantidadVendida;
            contexto.getRepositorioZapatilla().actualizarStock(zapatilla.getId(), nuevoStock);
            zapatilla.setStock(nuevoStock); // Actualizar el objeto en memoria

            System.out.println("Stock actualizado. Nuevo stock: " + nuevoStock);
            contexto.cambiarEstado(new EstadoPagado());
            System.out.println("Transición de estado: Pendiente -> Pagado.");
        } else {
            System.err.println("Error: Stock insuficiente para completar la venta.");
            // Opcionalmente, se podría anular automáticamente.
            contexto.solicitarAnulacion();
        }
    }

    @Override
    public void anular(ContextoVenta contexto) {
        System.out.println("Anulando venta pendiente...");
        contexto.cambiarEstado(new EstadoAnulado());
        System.out.println("Transición de estado: Pendiente -> Anulado.");
    }
}

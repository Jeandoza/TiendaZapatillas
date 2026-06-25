package pe.edu.utp.tiendazapatillas.modelo.estados;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;

/**
 * [STATE] Clase Contexto.
 * Mantiene una instancia de un estado concreto y delega el comportamiento a este.
 */
public class ContextoVenta {

    private EstadoVenta estadoActual;
    private final Zapatilla zapatilla;
    private final int cantidad;
    private final IRepositorioZapatilla repositorioZapatilla;

    public ContextoVenta(Zapatilla zapatilla, int cantidad, IRepositorioZapatilla repositorioZapatilla) {
        this.zapatilla = zapatilla;
        this.cantidad = cantidad;
        this.repositorioZapatilla = repositorioZapatilla;
        // El estado inicial siempre es Pendiente
        this.estadoActual = new EstadoPendiente();
    }

    public void cambiarEstado(EstadoVenta nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void solicitarRegistro() {
        estadoActual.registrar(this);
    }

    public void solicitarPago() {
        estadoActual.pagar(this);
    }

    public void solicitarAnulacion() {
        estadoActual.anular(this);
    }

    public Zapatilla getZapatilla() {
        return zapatilla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public IRepositorioZapatilla getRepositorioZapatilla() {
        return repositorioZapatilla;
    }

    public EstadoVenta getEstadoActual() {
        return estadoActual;
    }
}

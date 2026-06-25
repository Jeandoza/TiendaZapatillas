package pe.edu.utp.tiendazapatillas.servicio.notificaciones;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import java.util.ArrayList;
import java.util.List;

/**
 * [OBSERVER] Sujeto (Subject).
 * Mantiene una lista de observadores y notifica los cambios de estado.
 */
public class ServicioInventario {

    private final List<ObservadorStock> observadores = new ArrayList<>();

    public void registrarObservador(ObservadorStock observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(ObservadorStock observador) {
        observadores.remove(observador);
    }

    public void notificarCambioStock(Zapatilla zapatilla) {
        System.out.println("Notificando a los observadores sobre cambio de stock en: " + zapatilla.getModelo());
        for (ObservadorStock observador : observadores) {
            observador.onStockActualizado(zapatilla);
        }
    }
}

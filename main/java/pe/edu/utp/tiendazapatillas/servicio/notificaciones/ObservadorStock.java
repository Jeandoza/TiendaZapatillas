package pe.edu.utp.tiendazapatillas.servicio.notificaciones;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;

/**
 * [OBSERVER] Interfaz Observer.
 * Define el método que el sujeto llamará cuando ocurra un evento.
 */
public interface ObservadorStock {
    void onStockActualizado(Zapatilla zapatilla);
}

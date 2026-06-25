package pe.edu.utp.tiendazapatillas.servicio.proxy;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import java.util.List;
import java.util.Optional;

/**
 * [PROXY] Interfaz del Sujeto.
 * Define las operaciones que tanto el servicio real como el proxy deben implementar.
 */
public interface IAccesoZapatilla {
    List<Zapatilla> listarTodas();
    Optional<Zapatilla> buscarPorId(int id);
    boolean validarStock(int idZapatilla, int cantidad);
}

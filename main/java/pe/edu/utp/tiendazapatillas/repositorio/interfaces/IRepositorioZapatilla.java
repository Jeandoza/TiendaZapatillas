package pe.edu.utp.tiendazapatillas.repositorio.interfaces;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;

import java.util.List;
import java.util.Optional;

public interface IRepositorioZapatilla {
    List<Zapatilla> listarTodas();
    Optional<Zapatilla> buscarPorId(int id);
    void guardar(Zapatilla zapatilla);
    void actualizar(Zapatilla zapatilla);
    void eliminar(int id);
    void actualizarStock(int idZapatilla, int nuevoStock);
}

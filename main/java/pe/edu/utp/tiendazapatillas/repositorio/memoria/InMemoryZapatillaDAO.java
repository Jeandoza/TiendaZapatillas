package pe.edu.utp.tiendazapatillas.repositorio.memoria;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Marca;
import pe.edu.utp.tiendazapatillas.modelo.entidades.Talla;
import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Una implementación en memoria del repositorio de zapatillas para pruebas y desarrollo
 * sin necesidad de una base de datos.
 */
public class InMemoryZapatillaDAO implements IRepositorioZapatilla {

    private final List<Zapatilla> zapatillas = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public InMemoryZapatillaDAO() {
        // Precargar con datos de prueba
        Marca nike = new Marca(1, "Nike");
        Marca adidas = new Marca(2, "Adidas");
        Talla t41 = new Talla(1, "41");
        Talla t42 = new Talla(2, "42");

        zapatillas.add(new Zapatilla(idCounter.getAndIncrement(), "Air Max 90", nike, t42, new BigDecimal("450.00"), 20));
        zapatillas.add(new Zapatilla(idCounter.getAndIncrement(), "Superstar", adidas, t41, new BigDecimal("380.50"), 15));
        zapatillas.add(new Zapatilla(idCounter.getAndIncrement(), "Air Force 1 LX", nike, t41, new BigDecimal("550.00"), 5));
    }

    @Override
    public List<Zapatilla> listarTodas() {
        return new ArrayList<>(zapatillas); // Devuelve una copia para evitar modificaciones externas
    }

    @Override
    public Optional<Zapatilla> buscarPorId(int id) {
        return zapatillas.stream()
                .filter(z -> z.getId() == id)
                .findFirst();
    }

    @Override
    public void guardar(Zapatilla zapatilla) {
        zapatilla.setId(idCounter.getAndIncrement());
        zapatillas.add(zapatilla);
    }

    @Override
    public void actualizar(Zapatilla zapatilla) {
        buscarPorId(zapatilla.getId()).ifPresent(existente -> {
            existente.setModelo(zapatilla.getModelo());
            existente.setMarca(zapatilla.getMarca());
            existente.setTalla(zapatilla.getTalla());
            existente.setPrecio(zapatilla.getPrecio());
            existente.setStock(zapatilla.getStock());
        });
    }

    @Override
    public void eliminar(int id) {
        zapatillas.removeIf(z -> z.getId() == id);
    }

    @Override
    public void actualizarStock(int idZapatilla, int nuevoStock) {
        buscarPorId(idZapatilla).ifPresent(z -> z.setStock(nuevoStock));
    }
}

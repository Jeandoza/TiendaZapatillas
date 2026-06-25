package pe.edu.utp.tiendazapatillas.servicio.proxy;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;

import java.util.List;
import java.util.Optional;

/**
 * [PROXY] Sujeto Real.
 * La clase de servicio real que realiza el trabajo pesado.
 */
public class ServicioZapatillaReal implements IAccesoZapatilla {

    private final IRepositorioZapatilla repositorio;

    public ServicioZapatillaReal(IRepositorioZapatilla repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Zapatilla> listarTodas() {
        return repositorio.listarTodas();
    }

    @Override
    public Optional<Zapatilla> buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public boolean validarStock(int idZapatilla, int cantidad) {
        Optional<Zapatilla> zapatillaOpt = repositorio.buscarPorId(idZapatilla);
        return zapatillaOpt.map(zapatilla -> zapatilla.getStock() >= cantidad).orElse(false);
    }
}

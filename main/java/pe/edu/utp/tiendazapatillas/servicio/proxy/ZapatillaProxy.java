package pe.edu.utp.tiendazapatillas.servicio.proxy;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * [PROXY] Proxy.
 * Controla el acceso al Sujeto Real. Puede añadir lógica antes o después
 * de delegar la llamada, como comprobaciones de seguridad.
 */
public class ZapatillaProxy implements IAccesoZapatilla {

    private IAccesoZapatilla servicioReal;
    private final String rolUsuario;
    private final IRepositorioZapatilla repositorio;

    public ZapatillaProxy(String rolUsuario, IRepositorioZapatilla repositorio) {
        this.rolUsuario = rolUsuario;
        this.repositorio = repositorio;
    }

    private IAccesoZapatilla getServicioReal() {
        if (servicioReal == null) {
            // Inyecta el mismo repositorio al servicio real
            servicioReal = new ServicioZapatillaReal(repositorio);
        }
        return servicioReal;
    }

    @Override
    public List<Zapatilla> listarTodas() {
        if ("Administrador".equalsIgnoreCase(rolUsuario)) {
            return getServicioReal().listarTodas();
        } else {
            System.out.println("Acceso denegado: Solo los administradores pueden listar todas las zapatillas.");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Zapatilla> buscarPorId(int id) {
        // Todos los roles pueden buscar por ID
        return getServicioReal().buscarPorId(id);
    }

    @Override
    public boolean validarStock(int idZapatilla, int cantidad) {
        if ("Administrador".equalsIgnoreCase(rolUsuario) || "Cajero".equalsIgnoreCase(rolUsuario)) {
            return getServicioReal().validarStock(idZapatilla, cantidad);
        } else {
            System.out.println("Acceso denegado: Rol no autorizado para validar stock.");
            return false;
        }
    }
}

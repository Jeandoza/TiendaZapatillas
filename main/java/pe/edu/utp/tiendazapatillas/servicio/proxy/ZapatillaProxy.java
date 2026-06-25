package pe.edu.utp.tiendazapatillas.servicio.proxy;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * [PROXY] Controla el acceso al Sujeto Real y añade una capa de auditoría.
 */
public class ZapatillaProxy implements IAccesoZapatilla {

    private IAccesoZapatilla servicioReal;
    private final String rolUsuario;
    private final IRepositorioZapatilla repositorio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ZapatillaProxy(String rolUsuario, IRepositorioZapatilla repositorio) {
        this.rolUsuario = rolUsuario;
        this.repositorio = repositorio;
    }

    private void auditar(String operacion) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("AUDITORÍA: [%s] Rol '%s' ejecutó la operación '%s'.\n", timestamp, rolUsuario, operacion);
    }

    private IAccesoZapatilla getServicioReal() {
        if (servicioReal == null) {
            servicioReal = new ServicioZapatillaReal(repositorio);
        }
        return servicioReal;
    }

    @Override
    public List<Zapatilla> listarTodas() {
        // REQUERIMIENTO: Ambos roles pueden ver el catálogo.
        if ("Administrador".equalsIgnoreCase(rolUsuario) || "Cajero".equalsIgnoreCase(rolUsuario)) {
            auditar("Listar Todas las Zapatillas");
            return getServicioReal().listarTodas();
        } else {
            // REQUERIMIENTO: Se restringe a un rol no autorizado como 'Invitado'.
            System.out.println("ACCESO DENEGADO: El rol '" + rolUsuario + "' no tiene permisos para listar zapatillas.");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Zapatilla> buscarPorId(int id) {
        auditar("Buscar Zapatilla por ID: " + id);
        return getServicioReal().buscarPorId(id);
    }

    @Override
    public boolean validarStock(int idZapatilla, int cantidad) {
        if ("Administrador".equalsIgnoreCase(rolUsuario) || "Cajero".equalsIgnoreCase(rolUsuario)) {
            auditar(String.format("Validar Stock (ID: %d, Cantidad: %d)", idZapatilla, cantidad));
            return getServicioReal().validarStock(idZapatilla, cantidad);
        } else {
            System.out.println("ACCESO DENEGADO: El rol '" + rolUsuario + "' no tiene permisos para validar stock.");
            return false;
        }
    }
}

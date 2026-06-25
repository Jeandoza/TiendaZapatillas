package pe.edu.utp.tiendazapatillas.servicio.fachada;

import pe.edu.utp.tiendazapatillas.modelo.comprobantes.Comprobante;
import pe.edu.utp.tiendazapatillas.modelo.entidades.VentaHistorial;
import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.modelo.estados.ContextoVenta;
import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;
import pe.edu.utp.tiendazapatillas.servicio.fabricas.FabricaComprobante;
import pe.edu.utp.tiendazapatillas.servicio.notificaciones.ServicioInventario;
import pe.edu.utp.tiendazapatillas.servicio.proxy.IAccesoZapatilla;
import pe.edu.utp.tiendazapatillas.servicio.proxy.ZapatillaProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcesadorVentaFachada {

    private final IAccesoZapatilla servicioZapatilla;
    private final IRepositorioZapatilla repositorioZapatilla;
    private final ServicioInventario servicioInventario;
    private final Map<Integer, VentaHistorial> historialVentas = new HashMap<>();
    private final AtomicInteger ventaIdCounter = new AtomicInteger(1);

    public ProcesadorVentaFachada(String rolUsuario, IRepositorioZapatilla repositorioZapatilla, ServicioInventario servicioInventario) {
        this.repositorioZapatilla = repositorioZapatilla;
        this.servicioZapatilla = new ZapatillaProxy(rolUsuario, this.repositorioZapatilla);
        this.servicioInventario = servicioInventario;
    }

    public Optional<ResultadoVenta> realizarVenta(List<ComponenteZapatilla> carrito, FabricaComprobante.TipoComprobante tipoComprobante) {
        if (carrito == null || carrito.isEmpty()) {
            return Optional.empty();
        }

        // Asumimos una venta de un solo tipo de zapatilla por simplicidad de la demostración del State
        ComponenteZapatilla item = carrito.get(0);
        Zapatilla zapatilla = item.getZapatilla();
        int cantidad = 1; // Asumimos cantidad 1 por item en carrito

        if (!servicioZapatilla.validarStock(zapatilla.getId(), cantidad)) {
            return Optional.empty();
        }

        Comprobante comprobante = FabricaComprobante.crearComprobante(tipoComprobante, carrito);
        ContextoVenta contextoVenta = new ContextoVenta(zapatilla, cantidad, repositorioZapatilla);

        contextoVenta.solicitarRegistro();
        contextoVenta.solicitarPago();

        servicioInventario.notificarCambioStock(zapatilla);

        int nuevaVentaId = ventaIdCounter.getAndIncrement();
        VentaHistorial ventaGuardada = new VentaHistorial(nuevaVentaId, comprobante.getTotal(), contextoVenta);
        historialVentas.put(nuevaVentaId, ventaGuardada);

        return Optional.of(new ResultadoVenta(ventaGuardada, comprobante.generarContenido()));
    }

    public boolean procesarAnulacion(int ventaId) {
        VentaHistorial venta = historialVentas.get(ventaId);
        if (venta != null) {
            ContextoVenta contexto = venta.contexto();
            String estadoAntes = contexto.getEstadoActual().getClass().getSimpleName();
            contexto.solicitarAnulacion(); // El estado cambia aquí
            String estadoDespues = contexto.getEstadoActual().getClass().getSimpleName();

            if (!estadoAntes.equals(estadoDespues)) {
                servicioInventario.notificarCambioStock(contexto.getZapatilla());
                return true;
            }
        }
        return false;
    }
    
    public VentaHistorial getVentaHistorial(int id) {
        return historialVentas.get(id);
    }
}

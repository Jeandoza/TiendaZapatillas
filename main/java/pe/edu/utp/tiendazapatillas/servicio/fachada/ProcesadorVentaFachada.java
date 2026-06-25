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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProcesadorVentaFachada {

    // Clase interna para manejar los ítems del carrito, duplicada para que la fachada la entienda
    public record ItemCarrito(ComponenteZapatilla zapatilla, int cantidad) {}

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

    // La firma del método ahora acepta una lista genérica, compatible con el record del controlador
    public Optional<ResultadoVenta> realizarVenta(List<?> carritoItems, FabricaComprobante.TipoComprobante tipoComprobante) {
        if (carritoItems == null || carritoItems.isEmpty()) {
            return Optional.empty();
        }

        // Convertir la lista genérica a nuestra lista de ItemCarrito
        List<ItemCarrito> carrito;
        try {
            carrito = (List<ItemCarrito>) carritoItems;
        } catch (ClassCastException e) {
            // Manejar error si el tipo no es el esperado
            return Optional.empty();
        }

        // Validar stock para todos los productos antes de procesar
        for (ItemCarrito item : carrito) {
            if (!servicioZapatilla.validarStock(item.zapatilla().getZapatilla().getId(), item.cantidad())) {
                // Podríamos lanzar una excepción o devolver un mensaje de error específico
                System.err.println("Stock insuficiente para: " + item.zapatilla().getDescripcion());
                return Optional.empty();
            }
        }

        // Procesar cada item
        for (ItemCarrito item : carrito) {
            Zapatilla zapatilla = item.zapatilla().getZapatilla();
            int cantidad = item.cantidad();

            ContextoVenta contextoVenta = new ContextoVenta(zapatilla, cantidad, repositorioZapatilla);
            contextoVenta.solicitarRegistro();
            contextoVenta.solicitarPago(); // Esto descuenta el stock

            servicioInventario.notificarCambioStock(zapatilla);
        }

        // Crear un único comprobante para toda la venta
        List<ComponenteZapatilla> itemsParaComprobante = carrito.stream()
                .flatMap(item -> java.util.stream.Stream.generate(item::zapatilla).limit(item.cantidad()))
                .collect(Collectors.toList());

        Comprobante comprobante = FabricaComprobante.crearComprobante(tipoComprobante, itemsParaComprobante);

        int nuevaVentaId = ventaIdCounter.getAndIncrement();
        // Para el historial, guardamos el contexto del primer item como representativo
        VentaHistorial ventaGuardada = new VentaHistorial(nuevaVentaId, comprobante.getTotal(), new ContextoVenta(carrito.get(0).zapatilla().getZapatilla(), carrito.get(0).cantidad(), repositorioZapatilla));
        historialVentas.put(nuevaVentaId, ventaGuardada);

        return Optional.of(new ResultadoVenta(ventaGuardada, comprobante.generarContenido()));
    }

    public boolean procesarAnulacion(int ventaId) {
        VentaHistorial venta = historialVentas.get(ventaId);
        if (venta != null) {
            ContextoVenta contexto = venta.contexto();
            String estadoAntes = contexto.getEstadoActual().getClass().getSimpleName();
            contexto.solicitarAnulacion();
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

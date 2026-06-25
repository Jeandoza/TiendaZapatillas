package pe.edu.utp.tiendazapatillas;

import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.memoria.InMemoryZapatillaDAO;
import pe.edu.utp.tiendazapatillas.repositorio.mysql.MySQLZapatillaDAO;
import pe.edu.utp.tiendazapatillas.servicio.fachada.ProcesadorVentaFachada;
import pe.edu.utp.tiendazapatillas.servicio.notificaciones.ServicioInventario;
import pe.edu.utp.tiendazapatillas.servicio.proxy.IAccesoZapatilla;
import pe.edu.utp.tiendazapatillas.servicio.proxy.ZapatillaProxy;
import pe.edu.utp.tiendazapatillas.vista.components.PanelAlmacenInventario;
import pe.edu.utp.tiendazapatillas.vista.components.PanelCaja;
import pe.edu.utp.tiendazapatillas.vista.components.VentanaPrincipalFrame;
import pe.edu.utp.tiendazapatillas.vista.controlador.ControladorVenta;

import javax.swing.*;

public class Main {

    // Punto de control central para cambiar entre modo de prueba y producción.
    private static final boolean USAR_MYSQL = false;

    private static VentanaPrincipalFrame ventana;

    public static void main(String[] args) {
        // Configurar el Look and Feel del sistema para una mejor apariencia.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lanzar la aplicación en el hilo de despacho de eventos de Swing.
        SwingUtilities.invokeLater(() -> {
            ventana = new VentanaPrincipalFrame();
            ventana.addRoleChangeListener(e -> inicializarComponentesParaRol(ventana.getSelectedRole()));
            inicializarComponentesParaRol(ventana.getSelectedRole()); // Carga inicial
            ventana.setVisible(true);
        });
    }

    private static void inicializarComponentesParaRol(String rolUsuario) {
        // --- MODELO ---
        // Se decide qué implementación del repositorio usar basado en la bandera.
        IRepositorioZapatilla repositorio = USAR_MYSQL ? new MySQLZapatillaDAO() : new InMemoryZapatillaDAO();
        
        // Se crean los servicios y la fachada.
        ServicioInventario servicioInventario = new ServicioInventario();
        IAccesoZapatilla servicioZapatillaProxy = new ZapatillaProxy(rolUsuario, repositorio);
        ProcesadorVentaFachada fachada = new ProcesadorVentaFachada(rolUsuario, repositorio, servicioInventario);

        // --- VISTA ---
        // Se crean los paneles de la interfaz de usuario.
        PanelAlmacenInventario panelAlmacen = new PanelAlmacenInventario(rolUsuario, servicioZapatillaProxy);
        PanelCaja panelCaja = new PanelCaja();

        // --- CONTROLADOR ---
        // Se crea el controlador y se le inyectan las vistas y el modelo.
        new ControladorVenta(panelCaja, panelAlmacen, fachada);

        // Registrar el panel de almacén como observador para recibir notificaciones de stock.
        servicioInventario.registrarObservador(panelAlmacen);

        // --- ENSAMBLAJE DE LA UI ---
        // Se crea el panel divisor y se añaden los componentes de la vista.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelAlmacen, panelCaja);
        splitPane.setResizeWeight(0.55);

        // Se establece el panel ensamblado como el contenido principal de la ventana.
        ventana.setMainPanel(splitPane);
    }
}

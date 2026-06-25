package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.infraestructura.basedatos.ConexionBD;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.memoria.InMemoryZapatillaDAO;
import pe.edu.utp.tiendazapatillas.repositorio.mysql.MySQLZapatillaDAO;
import pe.edu.utp.tiendazapatillas.servicio.fachada.ProcesadorVentaFachada;
import pe.edu.utp.tiendazapatillas.servicio.notificaciones.ServicioInventario;
import pe.edu.utp.tiendazapatillas.servicio.proxy.IAccesoZapatilla;
import pe.edu.utp.tiendazapatillas.servicio.proxy.ZapatillaProxy;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipalFrame extends JFrame {

    private static final boolean USAR_MYSQL = false;
    private JComboBox<String> cmbRoles;
    private JPanel mainPanel;

    public VentanaPrincipalFrame() {
        setTitle("Tienda de Zapatillas - Demostración de Patrones de Diseño");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barra de Herramientas Superior (Control de Rol y Singleton)
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        cmbRoles = new JComboBox<>(new String[]{"ADMINISTRADOR", "CAJERO"});
        JLabel lblRol = new JLabel("Rol Actual: ");
        JLabel lblSingleton = new JLabel(" Instancia de BD (Singleton): " + ConexionBD.getInstance().hashCode() + " ");
        lblSingleton.setBorder(BorderFactory.createEtchedBorder());

        toolBar.add(lblRol);
        toolBar.add(cmbRoles);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(lblSingleton);
        add(toolBar, BorderLayout.NORTH);

        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        cmbRoles.addActionListener(e -> inicializarComponentesParaRol((String) cmbRoles.getSelectedItem()));
        cmbRoles.setSelectedItem("ADMINISTRADOR");
    }

    private void inicializarComponentesParaRol(String rolUsuario) {
        mainPanel.removeAll();

        IRepositorioZapatilla repositorio = USAR_MYSQL ? new MySQLZapatillaDAO() : new InMemoryZapatillaDAO();
        ServicioInventario servicioInventario = new ServicioInventario();
        IAccesoZapatilla servicioZapatillaProxy = new ZapatillaProxy(rolUsuario, repositorio);
        ProcesadorVentaFachada fachada = new ProcesadorVentaFachada(rolUsuario, repositorio, servicioInventario);

        PanelCatalogo panelCatalogo = new PanelCatalogo(rolUsuario, servicioZapatillaProxy);
        PanelCaja panelCaja = new PanelCaja(panelCatalogo, fachada);

        servicioInventario.registrarObservador(panelCatalogo);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCatalogo, panelCaja);
        splitPane.setResizeWeight(0.55);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipalFrame ventana = new VentanaPrincipalFrame();
            ventana.setVisible(true);
        });
    }
}

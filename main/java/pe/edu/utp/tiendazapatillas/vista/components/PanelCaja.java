package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.servicio.fabricas.FabricaComprobante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de la vista que contiene los componentes para la caja y ventas.
 * Ahora es una clase 'tonta' que solo muestra componentes y no tiene lógica de eventos.
 */
public class PanelCaja extends JPanel {

    private final DefaultTableModel modeloCarrito;
    private final JTable tablaCarrito;
    private final JComboBox<FabricaComprobante.TipoComprobante> cmbTipoComprobante;
    private final JTextArea areaComprobante;
    private final DefaultTableModel modeloHistorial;
    private final JTable tablaHistorial;
    private final JButton btnAgregarAlCarrito;
    private final JButton btnProcesarVenta;
    private final JButton btnAnularVenta;
    private final JSpinner spinnerCantidad;

    public PanelCaja() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Caja y Ventas"));

        // Panel Superior: Carrito y Controles de Venta
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Carrito de Compras"));

        modeloCarrito = new DefaultTableModel(new String[]{"Descripción", "Cantidad", "Precio Total"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        panelSuperior.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelControlesVenta = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAgregarAlCarrito = new JButton("Agregar al Carrito");
        
        // REQUERIMIENTO: Añadir JSpinner para la cantidad.
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 99, 1);
        spinnerCantidad = new JSpinner(spinnerModel);
        
        cmbTipoComprobante = new JComboBox<>(FabricaComprobante.TipoComprobante.values());
        btnProcesarVenta = new JButton("Procesar Venta");
        
        panelControlesVenta.add(new JLabel("Cantidad:"));
        panelControlesVenta.add(spinnerCantidad);
        panelControlesVenta.add(btnAgregarAlCarrito);
        panelControlesVenta.add(new JSeparator(SwingConstants.VERTICAL));
        panelControlesVenta.add(new JLabel("Comprobante:"));
        panelControlesVenta.add(cmbTipoComprobante);
        panelControlesVenta.add(btnProcesarVenta);
        panelSuperior.add(panelControlesVenta, BorderLayout.SOUTH);

        // Panel Central: Comprobante generado
        areaComprobante = new JTextArea(10, 40);
        areaComprobante.setEditable(false);
        areaComprobante.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollComprobante = new JScrollPane(areaComprobante);
        scrollComprobante.setBorder(BorderFactory.createTitledBorder("Comprobante Generado (Patrón Factory)"));

        // Panel Inferior: Historial y Anulación
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Historial de Ventas (Patrón State)"));
        modeloHistorial = new DefaultTableModel(new String[]{"ID Venta", "Total", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaHistorial = new JTable(modeloHistorial);
        panelInferior.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        btnAnularVenta = new JButton("Anular Venta Seleccionada");
        panelInferior.add(btnAnularVenta, BorderLayout.SOUTH);

        // Ensamblaje principal
        JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollComprobante, panelInferior);
        splitVertical.setResizeWeight(0.5);

        add(panelSuperior, BorderLayout.NORTH);
        add(splitVertical, BorderLayout.CENTER);
    }

    // --- Getters para que el Controlador acceda a los componentes ---

    public JSpinner getSpinnerCantidad() {
        return spinnerCantidad;
    }

    public DefaultTableModel getModeloCarrito() {
        return modeloCarrito;
    }

    public JComboBox<FabricaComprobante.TipoComprobante> getCmbTipoComprobante() {
        return cmbTipoComprobante;
    }

    public JTextArea getAreaComprobante() {
        return areaComprobante;
    }

    public DefaultTableModel getModeloHistorial() {
        return modeloHistorial;
    }

    public JTable getTablaHistorial() {
        return tablaHistorial;
    }

    public JButton getBtnAgregarAlCarrito() {
        return btnAgregarAlCarrito;
    }

    public JButton getBtnProcesarVenta() {
        return btnProcesarVenta;
    }

    public JButton getBtnAnularVenta() {
        return btnAnularVenta;
    }
}

package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.modelo.entidades.VentaHistorial;
import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;
import pe.edu.utp.tiendazapatillas.servicio.fachada.ProcesadorVentaFachada;
import pe.edu.utp.tiendazapatillas.servicio.fachada.ResultadoVenta;
import pe.edu.utp.tiendazapatillas.servicio.fabricas.FabricaComprobante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelCaja extends JPanel {

    private final PanelCatalogo panelCatalogo;
    private final ProcesadorVentaFachada fachada;

    private final DefaultTableModel modeloCarrito;
    private final JTable tablaCarrito;
    private final JComboBox<FabricaComprobante.TipoComprobante> cmbTipoComprobante;
    private final JTextArea areaComprobante;
    private final DefaultTableModel modeloHistorial;
    private final JTable tablaHistorial;

    private final List<ComponenteZapatilla> carrito = new ArrayList<>();

    public PanelCaja(PanelCatalogo panelCatalogo, ProcesadorVentaFachada fachada) {
        this.panelCatalogo = panelCatalogo;
        this.fachada = fachada;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Caja y Ventas"));

        // Panel Superior: Carrito y Controles de Venta
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Carrito de Compras"));

        modeloCarrito = new DefaultTableModel(new String[]{"Descripción", "Precio Final"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        panelSuperior.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelControlesVenta = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnAgregar = new JButton("Agregar al Carrito");
        cmbTipoComprobante = new JComboBox<>(FabricaComprobante.TipoComprobante.values());
        JButton btnProcesar = new JButton("Procesar Venta");
        panelControlesVenta.add(btnAgregar);
        panelControlesVenta.add(new JLabel("Comprobante:"));
        panelControlesVenta.add(cmbTipoComprobante);
        panelControlesVenta.add(btnProcesar);
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

        JButton btnAnular = new JButton("Anular Venta Seleccionada");
        panelInferior.add(btnAnular, BorderLayout.SOUTH);

        // Ensamblaje principal
        JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollComprobante, panelInferior);
        splitVertical.setResizeWeight(0.5);

        add(panelSuperior, BorderLayout.NORTH);
        add(splitVertical, BorderLayout.CENTER);

        // Listeners
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        btnProcesar.addActionListener(e -> procesarVenta());
        btnAnular.addActionListener(e -> anularVenta());
    }

    private void agregarAlCarrito() {
        ComponenteZapatilla seleccion = panelCatalogo.getSeleccionDecorada();
        if (seleccion != null) {
            carrito.add(seleccion);
            modeloCarrito.addRow(new Object[]{seleccion.getDescripcion(), String.format("%.2f", seleccion.getPrecio())});
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una zapatilla del catálogo primero.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void procesarVenta() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FabricaComprobante.TipoComprobante tipo = (FabricaComprobante.TipoComprobante) cmbTipoComprobante.getSelectedItem();

        fachada.realizarVenta(new ArrayList<>(carrito), tipo).ifPresent(resultado -> {
            areaComprobante.setText(resultado.contenidoComprobante());

            VentaHistorial ventaHistorial = resultado.historial();
            modeloHistorial.addRow(new Object[]{ventaHistorial.id(), String.format("%.2f", ventaHistorial.total()), ventaHistorial.getEstado()});

            carrito.clear();
            modeloCarrito.setRowCount(0);
        });
    }

    private void anularVenta() {
        int selectedRow = tablaHistorial.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta del historial para anular.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ventaId = (int) modeloHistorial.getValueAt(selectedRow, 0);
        if (fachada.procesarAnulacion(ventaId)) {
            VentaHistorial vh = fachada.getVentaHistorial(ventaId);
            if (vh != null) {
                modeloHistorial.setValueAt(vh.getEstado(), selectedRow, 2);
            }
            JOptionPane.showMessageDialog(this, "Venta " + ventaId + " anulada con éxito. Stock restaurado.", "Anulación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "La venta no se puede anular (posiblemente ya está anulada).", "Anulación Fallida", JOptionPane.ERROR_MESSAGE);
        }
    }
}

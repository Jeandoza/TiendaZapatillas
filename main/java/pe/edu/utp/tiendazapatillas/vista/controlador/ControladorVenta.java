package pe.edu.utp.tiendazapatillas.vista.controlador;

import pe.edu.utp.tiendazapatillas.modelo.entidades.VentaHistorial;
import pe.edu.utp.tiendazapatillas.modelo.precios.ComponenteZapatilla;
import pe.edu.utp.tiendazapatillas.servicio.fachada.ProcesadorVentaFachada;
import pe.edu.utp.tiendazapatillas.servicio.fachada.ResultadoVenta;
import pe.edu.utp.tiendazapatillas.servicio.fabricas.FabricaComprobante;
import pe.edu.utp.tiendazapatillas.vista.components.PanelAlmacenInventario;
import pe.edu.utp.tiendazapatillas.vista.components.PanelCaja;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ControladorVenta implements ActionListener {

    // Clase interna para manejar los ítems del carrito
    private record ItemCarrito(ComponenteZapatilla zapatilla, int cantidad) {}

    private final PanelCaja panelCaja;
    private final PanelAlmacenInventario panelAlmacen;
    private final ProcesadorVentaFachada fachada;
    private final List<ItemCarrito> carrito = new ArrayList<>();

    public ControladorVenta(PanelCaja panelCaja, PanelAlmacenInventario panelAlmacen, ProcesadorVentaFachada fachada) {
        this.panelCaja = panelCaja;
        this.panelAlmacen = panelAlmacen;
        this.fachada = fachada;

        this.panelCaja.getBtnAgregarAlCarrito().addActionListener(this);
        this.panelCaja.getBtnProcesarVenta().addActionListener(this);
        this.panelCaja.getBtnAnularVenta().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == panelCaja.getBtnAgregarAlCarrito()) {
            agregarAlCarrito();
        } else if (source == panelCaja.getBtnProcesarVenta()) {
            procesarVenta();
        } else if (source == panelCaja.getBtnAnularVenta()) {
            anularVenta();
        }
    }

    private void agregarAlCarrito() {
        ComponenteZapatilla seleccion = panelAlmacen.getSeleccionDecorada();
        if (seleccion != null) {
            int cantidad = (int) panelCaja.getSpinnerCantidad().getValue();
            carrito.add(new ItemCarrito(seleccion, cantidad));

            BigDecimal precioTotalItem = seleccion.getPrecio().multiply(new BigDecimal(cantidad));
            panelCaja.getModeloCarrito().addRow(new Object[]{
                    seleccion.getDescripcion(),
                    cantidad,
                    String.format("%.2f", precioTotalItem)
            });
        } else {
            JOptionPane.showMessageDialog(panelCaja, "Seleccione una zapatilla del catálogo primero.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void procesarVenta() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(panelCaja, "El carrito está vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FabricaComprobante.TipoComprobante tipo = (FabricaComprobante.TipoComprobante) panelCaja.getCmbTipoComprobante().getSelectedItem();

        // La fachada ahora necesita la lista de ItemCarrito
        fachada.realizarVenta(carrito, tipo).ifPresent(resultado -> {
            panelCaja.getAreaComprobante().setText(resultado.contenidoComprobante());

            VentaHistorial ventaHistorial = resultado.historial();
            panelCaja.getModeloHistorial().addRow(new Object[]{ventaHistorial.id(), String.format("%.2f", ventaHistorial.total()), ventaHistorial.getEstado()});

            carrito.clear();
            panelCaja.getModeloCarrito().setRowCount(0);
        });
    }

    private void anularVenta() {
        int selectedRow = panelCaja.getTablaHistorial().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panelCaja, "Seleccione una venta del historial para anular.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ventaId = (int) panelCaja.getModeloHistorial().getValueAt(selectedRow, 0);
        if (fachada.procesarAnulacion(ventaId)) {
            VentaHistorial vh = fachada.getVentaHistorial(ventaId);
            if (vh != null) {
                panelCaja.getModeloHistorial().setValueAt(vh.getEstado(), selectedRow, 2);
            }
            JOptionPane.showMessageDialog(panelCaja, "Venta " + ventaId + " anulada con éxito. Stock restaurado.", "Anulación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panelCaja, "La venta no se puede anular (posiblemente ya está anulada).", "Anulación Fallida", JOptionPane.ERROR_MESSAGE);
        }
    }
}

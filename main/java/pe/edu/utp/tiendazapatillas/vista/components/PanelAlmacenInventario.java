package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.servicio.notificaciones.ObservadorStock;
import pe.edu.utp.tiendazapatillas.servicio.proxy.IAccesoZapatilla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * [OBSERVER] Observador Concreto.
 * Este panel se actualiza cuando recibe una notificación de cambio de stock.
 */
public class PanelAlmacenInventario extends JPanel implements ObservadorStock {

    private final JTable tablaInventario;
    private final DefaultTableModel modeloTabla;
    private final IAccesoZapatilla servicioZapatilla;
    private final String rolUsuario;

    public PanelAlmacenInventario(String rolUsuario, IAccesoZapatilla servicioZapatilla) {
        this.rolUsuario = rolUsuario;
        this.servicioZapatilla = servicioZapatilla; // Recibe el servicio (proxy) ya creado
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Inventario de Almacén"));

        String[] columnas = {"ID", "Modelo", "Marca", "Talla", "Precio", "Stock"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaInventario = new JTable(modeloTabla);

        add(new JScrollPane(tablaInventario), BorderLayout.CENTER);

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        List<Zapatilla> zapatillas = servicioZapatilla.listarTodas();
        actualizarTabla(zapatillas);
    }

    private void actualizarTabla(List<Zapatilla> zapatillas) {
        modeloTabla.setRowCount(0); // Limpiar tabla
        if (zapatillas.isEmpty() && "Cajero".equalsIgnoreCase(rolUsuario)) {
             modeloTabla.addRow(new Object[]{"Acceso", "denegado", "para", "este", "rol", "N/A"});
        } else {
            for (Zapatilla z : zapatillas) {
                modeloTabla.addRow(new Object[]{
                        z.getId(),
                        z.getModelo(),
                        z.getMarca().getNombre(),
                        z.getTalla().getDescripcion(),
                        z.getPrecio(),
                        z.getStock()
                });
            }
        }
    }

    @Override
    public void onStockActualizado(Zapatilla zapatillaActualizada) {
        // Swing es de un solo hilo, las actualizaciones de UI deben ir en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(zapatillaActualizada.getId())) {
                    modeloTabla.setValueAt(zapatillaActualizada.getStock(), i, 5); // Actualiza solo la celda de stock
                    tablaInventario.setRowSelectionInterval(i, i);
                    break;
                }
            }
        });
    }
}

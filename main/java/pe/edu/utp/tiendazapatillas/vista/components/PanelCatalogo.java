package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.modelo.precios.*;
import pe.edu.utp.tiendazapatillas.servicio.notificaciones.ObservadorStock;
import pe.edu.utp.tiendazapatillas.servicio.proxy.IAccesoZapatilla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class PanelCatalogo extends JPanel implements ObservadorStock {

    private final JTable tablaInventario;
    private final DefaultTableModel modeloTabla;
    private final IAccesoZapatilla servicioZapatilla;
    private final String rolUsuario;

    private final JCheckBox chkDescuento;
    private final JCheckBox chkEdicionEspecial;
    private final JLabel lblPrecioCalculado;
    private ComponenteZapatilla zapatillaSeleccionada;

    public PanelCatalogo(String rolUsuario, IAccesoZapatilla servicioZapatilla) {
        this.rolUsuario = rolUsuario;
        this.servicioZapatilla = servicioZapatilla;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Catálogo de Zapatillas (Patrones: Observer, Proxy)"));

        // Tabla de inventario
        String[] columnas = {"ID", "Modelo", "Marca", "Talla", "Precio Base", "Stock"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        tablaInventario = new JTable(modeloTabla);
        tablaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Panel de control del Decorator
        JPanel panelDecorator = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDecorator.setBorder(BorderFactory.createTitledBorder("Cálculo de Precio (Patrón Decorator)"));
        chkDescuento = new JCheckBox("Descuento Liquidación (-20%)");
        chkEdicionEspecial = new JCheckBox("Edición Especial (+ S/ 50.00)");
        lblPrecioCalculado = new JLabel("Precio Final Calculado: S/ 0.00");
        lblPrecioCalculado.setFont(new Font("Arial", Font.BOLD, 14));
        panelDecorator.add(chkDescuento);
        panelDecorator.add(chkEdicionEspecial);
        panelDecorator.add(Box.createHorizontalStrut(20));
        panelDecorator.add(lblPrecioCalculado);

        add(new JScrollPane(tablaInventario), BorderLayout.CENTER);
        add(panelDecorator, BorderLayout.SOUTH);

        configurarListeners();
        cargarDatosIniciales();
    }

    private void configurarListeners() {
        tablaInventario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInventario.getSelectedRow() != -1) {
                int selectedRow = tablaInventario.getSelectedRow();
                int zapatillaId = (int) modeloTabla.getValueAt(selectedRow, 0);
                servicioZapatilla.buscarPorId(zapatillaId).ifPresent(z -> {
                    this.zapatillaSeleccionada = new ZapatillaBase(z);
                    chkDescuento.setSelected(false);
                    chkEdicionEspecial.setSelected(false);
                    actualizarPrecioCalculado();
                });
            }
        });

        ActionListener decoratorListener = e -> actualizarPrecioCalculado();
        chkDescuento.addActionListener(decoratorListener);
        chkEdicionEspecial.addActionListener(decoratorListener);
    }

    private void actualizarPrecioCalculado() {
        if (zapatillaSeleccionada == null) return;

        ComponenteZapatilla zapatillaDecorada = new ZapatillaBase(zapatillaSeleccionada.getZapatilla());

        if (chkDescuento.isSelected()) {
            zapatillaDecorada = new DescuentoLiquidacion(zapatillaDecorada, new BigDecimal("20"));
        }
        if (chkEdicionEspecial.isSelected()) {
            zapatillaDecorada = new EdicionEspecial(zapatillaDecorada, new BigDecimal("50.00"));
        }

        this.zapatillaSeleccionada = zapatillaDecorada;
        lblPrecioCalculado.setText(String.format("Precio Final Calculado: S/ %.2f", zapatillaDecorada.getPrecio()));
    }

    private void cargarDatosIniciales() {
        List<Zapatilla> zapatillas = servicioZapatilla.listarTodas();
        actualizarTabla(zapatillas);
    }

    private void actualizarTabla(List<Zapatilla> zapatillas) {
        modeloTabla.setRowCount(0);
        if (zapatillas.isEmpty() && "CAJERO".equalsIgnoreCase(rolUsuario)) {
            modeloTabla.addRow(new Object[]{"N/A", "Acceso restringido para este rol", "", "", "", "N/A"});
        } else {
            for (Zapatilla z : zapatillas) {
                modeloTabla.addRow(new Object[]{
                        z.getId(), z.getModelo(), z.getMarca().getNombre(),
                        z.getTalla().getDescripcion(), z.getPrecio(), z.getStock()
                });
            }
        }
    }

    @Override
    public void onStockActualizado(Zapatilla zapatillaActualizada) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(zapatillaActualizada.getId())) {
                    modeloTabla.setValueAt(zapatillaActualizada.getStock(), i, 5);
                    tablaInventario.setRowSelectionInterval(i, i);
                    break;
                }
            }
        });
    }

    public ComponenteZapatilla getSeleccionDecorada() {
        return zapatillaSeleccionada;
    }
}

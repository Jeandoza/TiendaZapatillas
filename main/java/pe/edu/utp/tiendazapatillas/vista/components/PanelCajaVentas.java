package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.servicio.fabricas.FabricaComprobante;

import javax.swing.*;
import java.awt.*;

public class PanelCajaVentas extends JPanel {

    private final JTextField txtIdZapatilla;
    private final JTextField txtCantidad;
    private final JComboBox<FabricaComprobante.TipoComprobante> cmbTipoComprobante;
    private final JButton btnComprar;
    private final JTextArea areaResultado;

    public PanelCajaVentas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Panel de Ventas"));

        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        panelForm.add(new JLabel("ID Zapatilla:"));
        txtIdZapatilla = new JTextField();
        panelForm.add(txtIdZapatilla);

        panelForm.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelForm.add(txtCantidad);

        panelForm.add(new JLabel("Tipo Comprobante:"));
        cmbTipoComprobante = new JComboBox<>(FabricaComprobante.TipoComprobante.values());
        panelForm.add(cmbTipoComprobante);

        btnComprar = new JButton("Realizar Compra");
        panelForm.add(new JLabel()); // Espacio
        panelForm.add(btnComprar);

        // Panel de resultado
        areaResultado = new JTextArea(10, 40);
        areaResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultado);

        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public int getIdZapatilla() {
        return Integer.parseInt(txtIdZapatilla.getText());
    }

    public int getCantidad() {
        return Integer.parseInt(txtCantidad.getText());
    }

    public FabricaComprobante.TipoComprobante getTipoComprobante() {
        return (FabricaComprobante.TipoComprobante) cmbTipoComprobante.getSelectedItem();
    }

    public JButton getBotonComprar() {
        return btnComprar;
    }

    public void mostrarResultado(String texto) {
        areaResultado.setText(texto);
    }
}

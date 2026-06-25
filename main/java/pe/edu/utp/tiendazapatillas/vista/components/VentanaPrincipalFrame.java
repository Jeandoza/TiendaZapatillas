package pe.edu.utp.tiendazapatillas.vista.components;

import pe.edu.utp.tiendazapatillas.infraestructura.basedatos.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * La ventana principal de la aplicación. Ahora es un componente de vista 'puro'
 * que se configura desde el exterior (desde Main.java).
 */
public class VentanaPrincipalFrame extends JFrame {

    private final JComboBox<String> cmbRoles;
    private final JPanel mainPanel;

    public VentanaPrincipalFrame() {
        setTitle("Tienda de Zapatillas - Demostración de Patrones de Diseño");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barra de Herramientas Superior
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

        // Panel principal que será llenado desde Main
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setMainPanel(Component component) {
        mainPanel.removeAll();
        mainPanel.add(component, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public String getSelectedRole() {
        return (String) cmbRoles.getSelectedItem();
    }

    public void addRoleChangeListener(ActionListener listener) {
        cmbRoles.addActionListener(listener);
    }
}

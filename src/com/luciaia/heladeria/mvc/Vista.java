package com.luciaia.heladeria.mvc;

import com.github.lgooddatepicker.components.DatePicker;
import com.luciaia.heladeria.base.enums.SaborHelado;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private final static String TITULO_FRAME = "Heladería MVC";

    // productos
    JPanel JPanelProducto;
    JRadioButton radioHelado;
    JButton btnLimpiarProducto;
    JButton btnCancelarProducto;
    JButton btnGuardarProducto;
    JButton btnNuevoProducto;
    JButton btnEditaProducto;
    JButton btnEliminarProducto;
    JButton btnBorrarBBDDProducto;
    JPanel panelCard;
    PanelHelado panelHelado;
    JTextField txtNombreProducto;
    JTextField txtPrecioProducto;
    DatePicker dateCaducidad;
    DatePicker dateApertura;
    JComboBox comboProveedor;

    // empleados
    JPanel JPanelEmpleado;
    JTextField txtNombreEmpeado;
    JTextField txtApellidosEmpleado;
    JTextField txtEmailEmpleado;
    JTextField txtTelefonoEmpleado;
    JButton btnLimpiarEmpleado;
    JButton btnCancelarEmpleado;
    JButton btnGuardarEmpleado;
    JButton btnNuevoEmpleado;
    JButton btnEditarEmpleado;
    JButton btnEliminarEmpleado;
    JButton btnBorrarBBDDEmpleado;

    // clientes
    JPanel JPanelCliente;
    JTextField txtNombreCliente;
    JTextField txtApellidosCliente;
    JTextField txtEmailCliente;
    JTextField txtTelefonoCliente;
    JButton btnLimpiarCliente;
    JButton btnNuevoCliente;
    JButton btnEditarCliente;
    JButton btnEliminarCliente;
    JButton btnBorrarBBDDCliente;
    JButton btnCancelarCliente;
    JButton btnGuardarCliente;

    // Proveedores
    JPanel JPanelProveedor;
    JTextField txtNombreProveedor;
    JTextField txtContactoProveedor;
    JTextField txtEmailProveedor;
    JTextField txtTelefonoProveedor;
    JTextField txtDireccionProveedor;
    JButton btnLimpiarProveedor;
    JButton btnNuevoProveedor;
    JButton btnEditarProveedor;
    JButton btnEliminarProveedor;
    JButton btnBorrarBBDDProveedor;
    JButton btnCancelarProveedor;
    JButton btnGuardarProveedor;

    // Ventas
    JPanel JPanelVenta;
    JComboBox comboEmpleado;
    JComboBox comboCliente;
    JButton btnNuevoVenta;
    JButton btnEditarVenta;
    JButton btnEliminarVenta;
    JButton btnBorrarBBDDVenta;

    // tablas
    JList listHelado;
    JList listCliente;
    JList listVentaCliente;
    JList listProveedor;
    JList listEmpleado;
    JList listVentaEmpleado;
    JList listHeladosProveedor;
    JList listVenta;
    JList listLineasVenta;

    DefaultListModel dlmProducto;
    DefaultListModel dlmEmpleado;
    DefaultListModel dlmCliente;
    DefaultListModel dlmProveedor;
    DefaultListModel dlmVenta;
    DefaultListModel dlmVentaEmpleado;
    DefaultListModel dlmVentaCliente;
    DefaultListModel dlmProductoProveedor;
    DefaultListModel dlmVentaProducto;

    //menubar
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    JButton btnMostrarVentasEmpleado;
    JButton btnMostrarVentasCliente;
    JButton btnMostrarHeladosProveedor;
    JButton btnMostrarDetalle;


    public Vista() {
        super(TITULO_FRAME);
        initFrame();
    }

    public void initFrame() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Image icono = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("Icono.png")
        );

        setIconImage(icono);
        //radioHelado.setSelected(true);
        crearPanelCard();
        botonesVisibles();

        CardLayout cl = (CardLayout) (panelCard.getLayout());
        cl.show(panelCard, "Helado");

        //llamo menu
        setMenu();
        //cargo enumerados
        setEnumComboBox();
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setListModels();
    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");

        mbBar.setBackground(new Color(255, 203, 133));
        itemDesconectar.setBackground(new Color(255, 203, 133));
        itemSalir.setBackground(new Color(255, 203, 133));

        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setEnumComboBox() {
        for (SaborHelado constant : SaborHelado.values()) {
            panelHelado.comboSabor.addItem(constant.getValor());
        }
        panelHelado.comboSabor.setSelectedIndex(-1);
    }

    private void setListModels() {
        dlmProducto = new DefaultListModel();
        listHelado.setModel(dlmProducto);
        dlmEmpleado = new DefaultListModel();
        listEmpleado.setModel(dlmEmpleado);
        dlmCliente = new DefaultListModel();
        listCliente.setModel(dlmCliente);
        dlmProveedor = new DefaultListModel();
        listProveedor.setModel(dlmProveedor);
        dlmVenta = new DefaultListModel();
        listVenta.setModel(dlmVenta);
        dlmVentaEmpleado = new DefaultListModel();
        listVentaEmpleado.setModel(dlmVentaEmpleado);
        dlmVentaCliente = new DefaultListModel();
        listVentaCliente.setModel(dlmVentaCliente);
        dlmProductoProveedor = new DefaultListModel();
        listHeladosProveedor.setModel(dlmProductoProveedor);
        dlmVentaProducto = new DefaultListModel();
        listLineasVenta.setModel(dlmVentaProducto);
    }

    private void botonesVisibles() {
        btnCancelarProducto.setVisible(false);
        btnGuardarProducto.setVisible(false);
        btnCancelarEmpleado.setVisible(false);
        btnGuardarEmpleado.setVisible(false);
        btnCancelarCliente.setVisible(false);
        btnGuardarCliente.setVisible(false);
        btnCancelarProveedor.setVisible(false);
        btnGuardarProveedor.setVisible(false);
    }

    public void crearPanelCard() {
        panelHelado = new PanelHelado();
        panelHelado.conAzucarRadioButton.setSelected(true);
        panelCard.add(panelHelado.panel1, "Helado");
    }

    public void bloquearVista() {
        setEnabledRecursivo(panel1, false);
    }

    public void desbloquearVista() {
        setEnabledRecursivo(panel1, true);
    }

    private void setEnabledRecursivo(Container container, boolean enabled) {
        for (Component c : container.getComponents()) {
            c.setEnabled(enabled);
            if (c instanceof Container) {
                setEnabledRecursivo((Container) c, enabled);
            }
        }
    }

    public void bloquearVistaExceptoMenu() {
        bloquearVista();
        itemDesconectar.setEnabled(true);
        itemSalir.setEnabled(true);
    }
}

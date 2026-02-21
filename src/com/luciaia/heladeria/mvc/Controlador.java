package com.luciaia.heladeria.mvc;

import com.luciaia.heladeria.base.*;
import com.luciaia.heladeria.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Controlador implements ActionListener, ListSelectionListener, WindowListener {
    private Modelo modelo;
    private Vista vista;
    private VistaVenta vistaVenta;
    boolean editando;
    boolean conectado;
    Venta ventaActual;

    /***
     * Constructor de la clase
     * @param modelo modelo
     * @param vista vista
     */
    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaVenta = new VistaVenta(vista);
        this.editando = false;

        modelo.conectar();
        conectado = true;
        addActionListeners(this);
        addWindowListeners(this);
        refrescarTodo();
    }

    // COMPROBAR CAMPOS OBLIGATORIOS ---------------------------------------------------------------------------------------
    /***
     * Comrpueba si hay campos vacios en producto
     */
    private boolean hayCamposVaciosProducto() {
        try {
            if (!vista.txtNombreProducto.getText().isEmpty()
                    && !vista.txtPrecioProducto.getText().isEmpty()
                    && !vista.dateCaducidad.getText().isEmpty()
                    && !vista.comboProveedor.getSelectedItem().toString().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Comrpueba si hay campos vacios en helado
     */
    private boolean hayCamposVaciosHelado() {
        try {
            if (!vista.panelHelado.comboSabor.getSelectedItem().toString().isEmpty()
                    && !vista.panelHelado.litrosHeladoTxt.getText().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Comrpueba si hay campos vacios en empleado
     */
    private boolean hayCamposVaciosEmpleado() {
        return vista.txtNombreEmpeado.getText().isEmpty() || vista.txtEmailEmpleado.getText().isEmpty();
    }

    /***
     * Comrpueba si hay campos vacios en cliente
     */
    private boolean hayCamposVaciosCliente() {
        return vista.txtNombreCliente.getText().isEmpty() || vista.txtEmailCliente.getText().isEmpty();
    }

    /***
     * Comrpueba si hay campos vacios en proveedor
     */
    private boolean hayCamposVaciosProveedor() {
        return vista.txtNombreProveedor.getText().isEmpty()
                || vista.txtContactoProveedor.getText().isEmpty()
                || vista.txtEmailProveedor.getText().isEmpty();
    }

    /***
     * Comrpueba si hay campos vacios en venta
     */
    private boolean hayCamposVaciosVenta() {
        try {
            if (!vista.comboEmpleado.getSelectedItem().toString().isEmpty()
                    && !vista.comboCliente.getSelectedItem().toString().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Comrpueba si hay campos vacios en ventaProducto
     */
    private boolean hayCamposVaciosVentaProducto() {
        try {
            if (!vistaVenta.comboProducto.getSelectedItem().toString().isEmpty()
                    && !vistaVenta.txtCantidad.getText().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Valida los campos en helado
     */
    private boolean validarCamposProducto() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtNombreProducto.getText())
                || !Util.consultaValida(vista.txtPrecioProducto.getText())
                || !Util.consultaValida(vista.txtNombreProducto.getText())
                || !Util.consultaValida(vista.panelHelado.litrosHeladoTxt.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosProducto() || hayCamposVaciosHelado()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtNombreProducto.getText(), 50)) {
            Util.mensajeError("El nombre del helado excede el máximo de carácteres (max 50)", "Campos Incorrectos");
            return false;
        }

        if (!editando && modelo.heladoExiste(vista.txtNombreProducto.getText())) {
            Util.mensajeError("El nombre del helado ya está en uso", "Campos Incorrectos");
            return false;
        }

        if (!Util.esFloat(vista.txtPrecioProducto.getText())) {
            Util.mensajeError("Precio en formato de número decimal o entero (positivo)", "Campos Incorrectos");
            return false;
        }

        if (!Util.esFloat(vista.panelHelado.litrosHeladoTxt.getText())) {
            Util.mensajeError("Litros en formato de número decimal o entero (positivo)", "Campos Incorrectos");
            return false;
        }
        return true;
    }

    /***
     * Valida los campos en empleado
     */
    private boolean validarCamposEmpleado() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtNombreEmpeado.getText())
                || !Util.consultaValida(vista.txtApellidosEmpleado.getText())
                || !Util.consultaValida(vista.txtEmailEmpleado.getText())
                || !Util.consultaValida(vista.txtTelefonoEmpleado.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosEmpleado()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtEmailEmpleado.getText(), 100)) {
            Util.mensajeError("El email del empleado excede el máximo de carácteres (max 100)", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarEmail(vista.txtEmailEmpleado.getText())) {
            Util.mensajeError("El email del empleado no tiene un formato correcto", "Campos Incorrectos");
            return false;
        }

        if (!editando && modelo.empleadoExiste(vista.txtEmailEmpleado.getText())) {
            Util.mensajeError("El email del empleado ya está en uso", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtNombreEmpeado.getText(), 50)) {
            Util.mensajeError("El nombre del empleado excede el máximo de carácteres (max 50)", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtApellidosEmpleado.getText(), 100)) {
            Util.mensajeError("Los apellidos del empleado exceden el máximo de carácteres (max 100)", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarTelefono(vista.txtTelefonoEmpleado.getText())) {
            Util.mensajeError("El teléfono del empleado no tiene un formato correcto (9 dígitos)", "Campos Incorrectos");
            return false;
        }
        return true;
    }

    /***
     * Valida los campos en cliente
     */
    private boolean validarCamposCliente() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtNombreCliente.getText())
                || !Util.consultaValida(vista.txtApellidosCliente.getText())
                || !Util.consultaValida(vista.txtEmailCliente.getText())
                || !Util.consultaValida(vista.txtTelefonoCliente.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosCliente()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtEmailCliente.getText(), 100)) {
            Util.mensajeError("El email del cliente excede el máximo de carácteres (max 100)", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarEmail(vista.txtEmailCliente.getText())) {
            Util.mensajeError("El email del cliente no tiene un formato correcto", "Campos Incorrectos");
            return false;
        }

        if (!editando && modelo.clienteExiste(vista.txtEmailCliente.getText())) {
            Util.mensajeError("El email del cliente ya está en uso", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtNombreCliente.getText(), 50)) {
            Util.mensajeError("El nombre del cliente excede el máximo de carácteres (max 50)", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtApellidosCliente.getText(), 100)) {
            Util.mensajeError("Los apellidos del cliente exceden el máximo de carácteres (max 100)", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarTelefono(vista.txtTelefonoCliente.getText())) {
            Util.mensajeError("El teléfono del cliente no tiene un formato correcto (9 dígitos)", "Campos Incorrectos");
            return false;
        }
        return true;
    }

    /***
     * Valida los campos en proveedor
     */
    private boolean validarCamposProveedor() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtNombreProveedor.getText())
                || !Util.consultaValida(vista.txtContactoProveedor.getText())
                || !Util.consultaValida(vista.txtEmailProveedor.getText())
                || !Util.consultaValida(vista.txtTelefonoProveedor.getText())
                || !Util.consultaValida(vista.txtDireccionProveedor.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosProveedor()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtNombreProveedor.getText(), 50)) {
            Util.mensajeError("El nombre del proveedor excede el máximo de carácteres (max 50)", "Campos Incorrectos");
            return false;
        }

        if (!editando && modelo.proveedorExiste(vista.txtNombreProveedor.getText())) {
            Util.mensajeError("El nombre del proveedor ya está en uso", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtEmailProveedor.getText(), 100)) {
            Util.mensajeError("El email del proveedor excede el máximo de carácteres (max 100)", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarEmail(vista.txtEmailProveedor.getText())) {
            Util.mensajeError("El email del proveedor no tiene un formato correcto", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtContactoProveedor.getText(), 50)) {
            Util.mensajeError("El contacto del proveedor exceden el máximo de carácteres (max 50)", "Campos Incorrectos");
            return false;
        }

        if (!Util.longitudCorrecta(vista.txtDireccionProveedor.getText(), 200)) {
            Util.mensajeError("La dirección del proveedor exceden el máximo de carácteres (max 200)", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarTelefono(vista.txtTelefonoProveedor.getText())) {
            Util.mensajeError("El teléfono del proveedor no tiene un formato correcto (9 dígitos)", "Campos Incorrectos");
            return false;
        }
        return true;
    }

    /***
     * Valida los campos en ventaProducto
     */
    private boolean validarCamposVentaProducto() {
        // Consultas válidas
        if (!Util.consultaValida(vistaVenta.txtCantidad.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosVentaProducto()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.esEntero(vistaVenta.txtCantidad.getText())) {
            Util.mensajeError("Cantidad en formato de número entero (positivo)", "Campos Incorrectos");
            return false;
        }
        return true;
    }
    // COMPROBAR CAMPOS OBLIGATORIOS FIN ---------------------------------------------------------------------------------------



    // LIMPIAR PANELES ---------------------------------------------------------------------------------------
    /***
     * Resetea el panel de Producto
     */
    private void resetearProducto() {
        vista.txtNombreProducto.setEditable(true);

        vista.listHelado.setEnabled(true);
        vista.btnGuardarProducto.setVisible(false);
        vista.btnCancelarProducto.setVisible(false);
        vista.btnEditaProducto.setEnabled(true);
        vista.btnNuevoProducto.setEnabled(true);
        vista.btnBorrarBBDDProducto.setEnabled(true);
        vista.btnLimpiarProducto.setEnabled(true);

        limpiarCamposProducto();
        limpiarCamposHelado();
        refrescarProducto();
        editando = false;
    }

    /***
     * Resetea el panel de empleado
     */
    private void resetearEmpleado() {
        vista.txtEmailEmpleado.setEditable(true);
        vista.listEmpleado.setEnabled(true);
        vista.listVentaEmpleado.setEnabled(true);
        vista.btnGuardarEmpleado.setVisible(false);
        vista.btnCancelarEmpleado.setVisible(false);
        vista.btnEditarEmpleado.setEnabled(true);
        vista.btnNuevoEmpleado.setEnabled(true);
        vista.btnLimpiarEmpleado.setEnabled(true);
        vista.btnBorrarBBDDEmpleado.setEnabled(true);

        limpiarCamposEmpleado();
        refrescarEmpleado();
        editando = false;
    }

    /***
     * Resetea el panel de cliente
     */
    private void resetearCliente() {
        vista.txtEmailCliente.setEditable(true);
        vista.listCliente.setEnabled(true);
        vista.listVentaCliente.setEnabled(true);
        vista.btnGuardarCliente.setVisible(false);
        vista.btnCancelarCliente.setVisible(false);
        vista.btnEditarCliente.setEnabled(true);
        vista.btnNuevoCliente.setEnabled(true);
        vista.btnLimpiarCliente.setEnabled(true);
        vista.btnBorrarBBDDCliente.setEnabled(true);

        limpiarCamposCliente();
        refrescarCliente();
        editando = false;
    }

    /***
     * Resetea el panel de proveedor
     */
    private void resetearProveedor() {
        vista.txtNombreProveedor.setEditable(true);
        vista.listProveedor.setEnabled(true);
        vista.listHeladosProveedor.setEnabled(true);
        vista.btnGuardarProveedor.setVisible(false);
        vista.btnCancelarProveedor.setVisible(false);
        vista.btnEditarProveedor.setEnabled(true);
        vista.btnNuevoProveedor.setEnabled(true);
        vista.btnLimpiarProveedor.setEnabled(true);
        vista.btnBorrarBBDDProveedor.setEnabled(true);

        limpiarCamposProveedor();
        refrescarProveedor();
        editando = false;
    }

    /***
     * Resetea el panel de venta
     */
    private void resetearVenta() {
        vista.listVenta.setEnabled(true);
        vista.listLineasVenta.setEnabled(true);
        limpiarCamposVenta();
        refrescarVenta();
        editando = false;
    }

    /***
     * Resetea el panel de ventaProducto
     */
    private void resetearVentaProducto(Venta venta) {
        limpiarCamposVentaProducto();
        refrescarVentaProducto(venta);
        vistaVenta.listVentaProducto.setEnabled(true);
        vistaVenta.btnGuardar.setVisible(false);
        vistaVenta.btnCancelar.setVisible(false);
        vistaVenta.btnEditar.setEnabled(true);
        vistaVenta.btnAnnadir.setEnabled(true);
        vistaVenta.btnBorrarBBDDVentaProducto.setEnabled(true);
        editando = false;
    }

    /***
     * Limpiar los campos de producto
     */
    private void limpiarCamposProducto() {
        vista.txtNombreProducto.setText(null);
        vista.txtPrecioProducto.setText(null);
        vista.comboProveedor.setSelectedIndex(-1);
        vista.dateApertura.setText(null);
        vista.dateCaducidad.setText(null);
    }

    /***
     * Limpiar los campos de helado
     */
    private void limpiarCamposHelado() {
        vista.panelHelado.comboSabor.setSelectedIndex(-1);
        vista.panelHelado.conAzucarRadioButton.setSelected(true);
        vista.panelHelado.litrosHeladoTxt.setText(null);
    }

    /***
     * Limpiar los campos de empleado
     */
    private void limpiarCamposEmpleado() {
        vista.txtNombreEmpeado.setText(null);
        vista.txtApellidosEmpleado.setText(null);
        vista.txtEmailEmpleado.setText(null);
        vista.txtTelefonoEmpleado.setText(null);
    }

    /***
     * Limpiar los campos de cliente
     */
    private void limpiarCamposCliente() {
        vista.txtNombreCliente.setText(null);
        vista.txtApellidosCliente.setText(null);
        vista.txtEmailCliente.setText(null);
        vista.txtTelefonoCliente.setText(null);
    }

    /***
     * Limpiar los campos de proveedor
     */
    private void limpiarCamposProveedor() {
        vista.txtNombreProveedor.setText(null);
        vista.txtContactoProveedor.setText(null);
        vista.txtEmailProveedor.setText(null);
        vista.txtTelefonoProveedor.setText(null);
        vista.txtDireccionProveedor.setText(null);
    }

    /***
     * Limpiar los campos de venta
     */
    private void limpiarCamposVenta() {
        vista.comboEmpleado.setSelectedIndex(-1);
        vista.comboCliente.setSelectedIndex(-1);
    }

    /***
     * Limpiar los campos de ventaProducto
     */
    private void limpiarCamposVentaProducto() {
        vistaVenta.txtEmpleadoVenta.setText(String.valueOf(vista.comboEmpleado.getSelectedItem()));
        vistaVenta.txtClienteVenta.setText(String.valueOf(vista.comboCliente.getSelectedItem()));
        vistaVenta.comboProducto.setSelectedIndex(-1);
        vistaVenta.txtCantidad.setText(null);
    }
    // LIMPIAR PANELES FIN ---------------------------------------------------------------------------------------


    // REFRESCAR TABLAS ---------------------------------------------------------------------------------------
    /***
     * Actualizar los campos
     */
    private void refrescarTodo() {
        refrescarProveedor();
        refrescarEmpleado();
        refrescarCliente();
        refrescarProducto();
        refrescarVenta();
    }

    /***
     * Listar los proveedores
     */
    private void refrescarProveedor() {
        vista.dlmProveedor.clear();
        ArrayList<Proveedor> lista = modelo.getProveedoresActivos();
        for (Proveedor proveedor : lista) {
            vista.dlmProveedor.addElement(proveedor);
        }
        vista.comboProveedor.removeAllItems();
        ArrayList<Proveedor> prov = modelo.getProveedoresActivos();

        for (Proveedor proveedor : prov) {
            vista.comboProveedor.addItem(proveedor);
        }
        vista.comboProveedor.setSelectedIndex(-1);
    }

    /***
     * Listar los helados por proveedor
     */
    private void listarHeladosProveedor() {
        int filaEditando = vista.listProveedor.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún proveedor seleccionado", "Selecciona un proveedor");
            return;
        }
        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        vista.dlmProductoProveedor.clear();
        ArrayList<Helado> lista = modelo.getHeladosProveedor(proveedor);
        for (Helado helado : lista) {
            vista.dlmProductoProveedor.addElement(helado);
        }
    }

    /***
     * Listar los empleados
     */
    private void refrescarEmpleado() {
        vista.dlmEmpleado.clear();
        ArrayList<Empleado> lista = modelo.getEmpleadosActivos();
        for (Empleado empleado : lista) {
            vista.dlmEmpleado.addElement(empleado);
        }
        vista.listEmpleado.setModel(vista.dlmEmpleado);
        vista.comboEmpleado.removeAllItems();
        ArrayList<Empleado> emp = modelo.getEmpleadosActivos();

        for (Empleado empleado : emp) {
            vista.comboEmpleado.addItem(empleado);
        }
        vista.comboEmpleado.setSelectedIndex(-1);
    }

    /***
     * Listar las ventas por empleado
     */
    private void listarVentasEmpleado() {
        int filaEditando = vista.listEmpleado.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún empleado seleccionado", "Selecciona un empleado");
            return;
        }
        Empleado empleado = (Empleado) vista.listEmpleado.getSelectedValue();
        vista.dlmVentaEmpleado.clear();
        ArrayList<Venta> lista = modelo.getVentasEmpleado(empleado);
        for (Venta venta : lista) {
            vista.dlmVentaEmpleado.addElement(venta);
        }
    }

    /***
     * Listar los clientes
     */
    private void refrescarCliente() {
        vista.dlmCliente.clear();
        ArrayList<Cliente> lista = modelo.getClientesActivos();
        for (Cliente cliente : lista) {
            vista.dlmCliente.addElement(cliente);
        }
        vista.comboCliente.removeAllItems();
        ArrayList<Cliente> cli = modelo.getClientesActivos();

        for (Cliente cliente : cli) {
            vista.comboCliente.addItem(cliente);
        }
        vista.comboCliente.setSelectedIndex(-1);
    }

    /***
     * Listar las ventas por cliente
     */
    private void listarVentasCliente() {
        int filaEditando = vista.listCliente.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún cliente seleccionado", "Selecciona un cliente");
            return;
        }
        Cliente cliente = (Cliente) vista.listCliente.getSelectedValue();
        vista.dlmVentaCliente.clear();
        ArrayList<Venta> lista = modelo.getVentasCliente(cliente);
        for (Venta venta : lista) {
            vista.dlmVentaCliente.addElement(venta);
        }
    }

    /***
     * Listar los helados
     */
    private void refrescarProducto() {
        vista.dlmProducto.clear();
        ArrayList<Helado> lista = modelo.getHeladosActivos();
        for (Helado helado : lista) {
            vista.dlmProducto.addElement(helado);
        }
        vistaVenta.comboProducto.removeAllItems();
        ArrayList<Helado> hel = modelo.getHeladosActivos();

        for (Helado helado : hel) {
            vistaVenta.comboProducto.addItem(helado);
        }
        vistaVenta.comboProducto.setSelectedIndex(-1);
    }

    /***
     * Listar las ventas
     */
    private void refrescarVenta() {
        ArrayList<Venta> lista = modelo.getVentas();
        vista.dlmVenta.clear();
        for (Venta venta : lista) {
            vista.dlmVenta.addElement(venta);
        }
    }

    /***
     * Listar los helados por proveedor
     */
    private void listarDetalle() {
        int filaEditando = vista.listVenta.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ninguna venta seleccionado", "Selecciona una venta");
            return;
        }
        Venta venta = (Venta) vista.listVenta.getSelectedValue();
        vista.dlmVentaProducto.clear();
        ArrayList<VentaProducto> lista = modelo.getLineasVentasVenta(venta);
        for (VentaProducto linea : lista) {
            vista.dlmVentaProducto.addElement(linea);
        }
    }

    /***
     * Listar las ventas Producto
     */
    private void refrescarVentaProducto(Venta venta) {
        ArrayList<VentaProducto> lista = modelo.getLineasVentasVenta(venta);
        vistaVenta.dlmVentaProducto.clear();
        for (VentaProducto v : lista) {
            vistaVenta.dlmVentaProducto.addElement(v);
        }
    }
    // REFRESCAR TABLAS FIN ---------------------------------------------------------------------------------------



    // NUEVO -----------------------------------------------------------------------------------------
    /***
     * Crea un helado
     */
    private void nuevoProducto() {
        if (!validarCamposProducto()) {
            return;
        }
        Helado helado = new Helado();
        helado.setNombre(vista.txtNombreProducto.getText());
        helado.setPrecio(Float.parseFloat(vista.txtPrecioProducto.getText()));
        helado.setFechaApertura(Date.valueOf(vista.dateApertura.getDate()));
        helado.setFechaCaducidad(Date.valueOf(vista.dateCaducidad.getDate()));
        helado.setProveedor((Proveedor) vista.comboProveedor.getSelectedItem());
        helado.setSabor(vista.panelHelado.comboSabor.getSelectedItem().toString());
        helado.setAzucar(vista.panelHelado.conAzucarRadioButton.isSelected());
        helado.setLitros(Float.parseFloat(vista.panelHelado.litrosHeladoTxt.getText()));
        helado.setActivo(true);
        modelo.insert(helado);

        resetearProducto();
        Util.mensajeInfo("Se ha añadido un nuevo helado", "Nuevo Helado");
    }

    /***
     * Crea un empleado
     */
    private void nuevoEmpleado() {
        if (!validarCamposEmpleado()) {
            return;
        }
        Empleado empleado = new Empleado();
        empleado.setNombre(vista.txtNombreEmpeado.getText());
        empleado.setApellidos(vista.txtApellidosEmpleado.getText());
        empleado.setEmail(vista.txtEmailEmpleado.getText());
        empleado.setTelefono(vista.txtTelefonoEmpleado.getText());
        empleado.setActivo(true);
        modelo.insert(empleado);

        resetearEmpleado();
        Util.mensajeInfo("Se ha añadido un nuevo empleado", "Nuevo Empleado");
    }

    /***
     * Crea un cliente
     */
    private void nuevoCliente() {
        if (!validarCamposCliente()) {
            return;
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(vista.txtNombreCliente.getText());
        cliente.setApellidos(vista.txtApellidosCliente.getText());
        cliente.setEmail(vista.txtEmailCliente.getText());
        cliente.setTelefono(vista.txtTelefonoCliente.getText());
        cliente.setActivo(true);
        modelo.insert(cliente);

        resetearCliente();
        Util.mensajeInfo("Se ha añadido un nuevo cliente", "Nuevo Cliente");
    }

    /***
     * Crea un proveedor
     */
    private void nuevoProveedor() {
        if (!validarCamposProveedor()) {
            return;
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(vista.txtNombreProveedor.getText());
        proveedor.setPersonaContacto(vista.txtContactoProveedor.getText());
        proveedor.setEmail(vista.txtEmailProveedor.getText());
        proveedor.setTelefono(vista.txtTelefonoProveedor.getText());
        proveedor.setDireccion(vista.txtDireccionProveedor.getText());
        proveedor.setActivo(true);
        modelo.insert(proveedor);

        resetearProveedor();
        Util.mensajeInfo("Se ha añadido un nuevo proveedor", "Nuevo Proveedor");
    }

    /***
     * Crea una venta
     */
    private void nuevoVenta() {
        if (hayCamposVaciosVenta()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return;
        }
        Venta venta = new Venta();
        venta.setCliente((Cliente) vista.comboCliente.getSelectedItem());
        venta.setEmpleado((Empleado) vista.comboEmpleado.getSelectedItem());
        modelo.insert(venta);

        resetearVentaProducto(modelo.ultimaVenta());
        vistaVenta.setVisible(true);
    }

    /***
     * Crea una ventaProducto
     */
    private void nuevoVentaProducto() {
        if (!validarCamposVentaProducto()) {
            return;
        }
        VentaProducto ventaProducto = new VentaProducto();
        ventaProducto.setCantidad(Integer.parseInt(vistaVenta.txtCantidad.getText()));
        ventaProducto.setVenta(modelo.ultimaVenta());
        ventaProducto.setHelado((Helado) vistaVenta.comboProducto.getSelectedItem());
        ventaProducto.setPrecioTotal(ventaProducto.getHelado().getPrecio() * ventaProducto.getCantidad());
        modelo.insert(ventaProducto);

        ventaActual = modelo.ultimaVenta();
        resetearVentaProducto(ventaActual);
        refrescarVenta();
        modelo.generateVenta(ventaActual.getId());
        Util.mensajeInfo("Se ha añadido un nuevo producto", "Nuevo Producto");
    }
    // NUEVO FIN -----------------------------------------------------------------------------------------


    // EDITAR -----------------------------------------------------------------------------------------
    /***
     * Modifica un helado
     */
    private void editarProducto() {
        int filaEditando = vista.listHelado.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún producto seleccionado", "Selecciona un producto");
            return;
        }
        Helado helado = (Helado) vista.listHelado.getSelectedValue();
        vista.txtNombreProducto.setText(helado.getNombre());
        vista.txtPrecioProducto.setText(String.valueOf(helado.getPrecio()));
        vista.dateApertura.setDate(LocalDate.parse(String.valueOf(helado.getFechaApertura())));
        vista.dateCaducidad.setDate(LocalDate.parse(String.valueOf(helado.getFechaCaducidad())));
        vista.comboProveedor.setSelectedItem(helado.getProveedor());
        vista.panelHelado.comboSabor.setSelectedItem(helado.getSabor());
        if (helado.isAzucar()) {
            vista.panelHelado.conAzucarRadioButton.setSelected(true);
        } else {
            vista.panelHelado.sinAzucarRadioButton.setSelected(true);
        }
        vista.panelHelado.litrosHeladoTxt.setText(String.valueOf(helado.getLitros()));

        vista.listHelado.setEnabled(false);
        vista.txtNombreProducto.setEditable(false);
        vista.btnGuardarProducto.setVisible(true);
        vista.btnCancelarProducto.setVisible(true);
        vista.btnEditaProducto.setEnabled(false);
        vista.btnNuevoProducto.setEnabled(false);
        vista.btnBorrarBBDDProducto.setEnabled(false);
        vista.btnLimpiarProducto.setEnabled(false);
        editando = true;
    }

    /***
     * Modifica un empleado
     */
    private void editarEmpleado() {
        int filaEditando = vista.listEmpleado.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún empleado seleccionado", "Selecciona un empleado");
            return;
        }

        Empleado empleado = (Empleado) vista.listEmpleado.getSelectedValue();
        vista.txtNombreEmpeado.setText(empleado.getNombre());
        vista.txtApellidosEmpleado.setText(empleado.getApellidos());
        vista.txtEmailEmpleado.setText(empleado.getEmail());
        vista.txtTelefonoEmpleado.setText(empleado.getTelefono());

        vista.listEmpleado.setEnabled(false);
        vista.txtEmailEmpleado.setEditable(false);

        vista.btnGuardarEmpleado.setVisible(true);
        vista.btnCancelarEmpleado.setVisible(true);
        vista.btnEditarEmpleado.setEnabled(false);
        vista.btnNuevoEmpleado.setEnabled(false);
        vista.btnBorrarBBDDEmpleado.setEnabled(false);
        vista.btnLimpiarEmpleado.setEnabled(false);
        editando = true;
    }

    /***
     * Modifica un cliente
     */
    private void editarCliente() {
        int filaEditando = vista.listCliente.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún cliente seleccionado", "Selecciona un cliente");
            return;
        }

        Cliente cliente = (Cliente) vista.listCliente.getSelectedValue();
        vista.txtNombreCliente.setText(cliente.getNombre());
        vista.txtApellidosCliente.setText(cliente.getApellidos());
        vista.txtEmailCliente.setText(cliente.getEmail());
        vista.txtTelefonoCliente.setText(cliente.getTelefono());

        vista.listCliente.setEnabled(false);
        vista.txtEmailCliente.setEditable(false);

        vista.btnGuardarCliente.setVisible(true);
        vista.btnCancelarCliente.setVisible(true);
        vista.btnEditarCliente.setEnabled(false);
        vista.btnNuevoCliente.setEnabled(false);
        vista.btnBorrarBBDDCliente.setEnabled(false);
        vista.btnLimpiarCliente.setEnabled(false);
        editando = true;
    }

    /***
     * Modifica un proveedor
     */
    private void editarProveedor() {
        int filaEditando = vista.listProveedor.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún proveedor seleccionado", "Selecciona un proveedor");
            return;
        }

        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        vista.txtNombreProveedor.setText(proveedor.getNombre());
        vista.txtContactoProveedor.setText(proveedor.getPersonaContacto());
        vista.txtEmailProveedor.setText(proveedor.getEmail());
        vista.txtTelefonoProveedor.setText(proveedor.getTelefono());
        vista.txtDireccionProveedor.setText(proveedor.getDireccion());

        vista.listProveedor.setEnabled(false);
        vista.txtNombreProveedor.setEditable(false);

        vista.btnGuardarProveedor.setVisible(true);
        vista.btnCancelarProveedor.setVisible(true);
        vista.btnEditarProveedor.setEnabled(false);
        vista.btnNuevoProveedor.setEnabled(false);
        vista.btnBorrarBBDDProveedor.setEnabled(false);
        vista.btnLimpiarProveedor.setEnabled(false);
        editando = true;
    }

    /***
     * Modifica una venta
     */
    private void editarVenta() {
        int filaEditando = vista.listVenta.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ninguna venta seleccionado", "Selecciona una venta");
            return;
        }

        Venta venta = (Venta) vista.listVenta.getSelectedValue();
        vista.comboEmpleado.setSelectedItem(venta.getEmpleado());
        vista.comboCliente.setSelectedItem(venta.getCliente());
        ventaActual = venta;

        resetearVentaProducto(venta);
        vistaVenta.setVisible(true);
    }

    /***
     * Modifica una ventaProducto
     */
    private void editarVentaProducto() {
        int filaEditando = vistaVenta.listVentaProducto.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ninguna línea seleccionado", "Selecciona una línea");
            return;
        }

        VentaProducto ventaProducto = (VentaProducto) vistaVenta.listVentaProducto.getSelectedValue();
        vistaVenta.txtEmpleadoVenta.setText(ventaProducto.getVenta().getEmpleado().getNombre());
        vistaVenta.txtClienteVenta.setText(ventaProducto.getVenta().getCliente().getNombre());
        vistaVenta.comboProducto.setSelectedItem(ventaProducto.getHelado());

        vistaVenta.txtCantidad.setText(String.valueOf(ventaProducto.getCantidad()));
        vistaVenta.listVentaProducto.setEnabled(false);
        vistaVenta.btnGuardar.setVisible(true);
        vistaVenta.btnCancelar.setVisible(true);
        vistaVenta.btnEditar.setEnabled(false);
        vistaVenta.btnAnnadir.setEnabled(false);
        vistaVenta.btnBorrarBBDDVentaProducto.setEnabled(false);
        editando = true;
    }
    // EDITAR FIN -----------------------------------------------------------------------------------------


    // GUARDAR -----------------------------------------------------------------------------------------
    /***
     * Guarda las modificaciones en un helado
     */
    private void guardarProducto() {
        if (!validarCamposProducto()) {
            return;
        }
        Helado helado = (Helado) vista.listHelado.getSelectedValue();
        helado.setNombre(vista.txtNombreProducto.getText());
        helado.setPrecio(Float.parseFloat(vista.txtPrecioProducto.getText()));
        helado.setFechaApertura(Date.valueOf(vista.dateApertura.getDate()));
        helado.setFechaCaducidad(Date.valueOf(vista.dateCaducidad.getDate()));
        helado.setProveedor((Proveedor) vista.comboProveedor.getSelectedItem());
        helado.setSabor(vista.panelHelado.comboSabor.getSelectedItem().toString());
        helado.setAzucar(vista.panelHelado.conAzucarRadioButton.isSelected());
        helado.setLitros(Float.parseFloat(vista.panelHelado.litrosHeladoTxt.getText()));
        helado.setActivo(true);
        modelo.update(helado);

        resetearProducto();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Producto");
    }

    /***
     * Guarda las modificaciones en un empleado
     */
    private void guardarEmpleado() {
        if (!validarCamposEmpleado()) {
            return;
        }
        Empleado empleado = (Empleado) vista.listEmpleado.getSelectedValue();
        empleado.setNombre(vista.txtNombreEmpeado.getText());
        empleado.setApellidos(vista.txtApellidosEmpleado.getText());
        empleado.setEmail(vista.txtEmailEmpleado.getText());
        empleado.setTelefono(vista.txtTelefonoEmpleado.getText());
        empleado.setActivo(true);
        modelo.update(empleado);

        resetearEmpleado();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Empleado");
    }

    /***
     * Guarda las modificaciones en un cliente
     */
    private void guardarCliente() {
        if (!validarCamposCliente()) {
            return;
        }
        Cliente cliente = (Cliente) vista.listCliente.getSelectedValue();
        cliente.setNombre(vista.txtNombreCliente.getText());
        cliente.setApellidos(vista.txtApellidosCliente.getText());
        cliente.setEmail(vista.txtEmailCliente.getText());
        cliente.setTelefono(vista.txtTelefonoCliente.getText());
        cliente.setActivo(true);
        modelo.update(cliente);

        resetearCliente();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Cliente");
    }

    /***
     * Guarda las modificaciones en un proveedor
     */
    private void guardarProveedor() {
        if (!validarCamposProveedor()) {
            return;
        }
        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        proveedor.setNombre(vista.txtNombreProveedor.getText());
        proveedor.setPersonaContacto(vista.txtContactoProveedor.getText());
        proveedor.setEmail(vista.txtEmailProveedor.getText());
        proveedor.setTelefono(vista.txtTelefonoProveedor.getText());
        proveedor.setDireccion(vista.txtDireccionProveedor.getText());
        proveedor.setActivo(true);
        modelo.update(proveedor);

        resetearProveedor();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Proveedor");
    }

    /***
     * Guarda las modificaciones en una ventaProducto
     */
    private void guardarVentaProducto() {
        if (!validarCamposVentaProducto()) {
            return;
        }
        VentaProducto ventaProducto = (VentaProducto) vistaVenta.listVentaProducto.getSelectedValue();
        ventaProducto.setCantidad(Integer.parseInt(vistaVenta.txtCantidad.getText()));
        ventaProducto.setVenta(ventaActual);
        ventaProducto.setHelado((Helado) vistaVenta.comboProducto.getSelectedItem());
        ventaProducto.setPrecioTotal(ventaProducto.getHelado().getPrecio() * ventaProducto.getCantidad());
        modelo.update(ventaProducto);

        resetearVentaProducto(ventaActual);
        refrescarVenta();
        modelo.generateVenta(ventaActual.getId());
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Venta - Producto");
    }
    // GUARDAR FIN -----------------------------------------------------------------------------------------


    // ELIMINAR -----------------------------------------------------------------------------------------
    /***
     * Elimina un helado
     */
    private void eliminarHelado() {
        int fila = vista.listHelado.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ningún producto seleccionado", "Selecciona un producto");
            return;
        }

        Helado helado = (Helado) vista.listHelado.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar \"" + helado.getNombre() + "\"?", "Eliminar producto");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.deleteHelado(helado.getId());
            refrescarProducto();
            if (editando) {
                resetearProducto();
            }
            Util.mensajeInfo("Se ha eliminado \"" + helado.getNombre() + "\"", "Producto Eliminado");
        }
    }

    /***
     * Elimina un empleado
     */
    private void eliminarEmpleado() {
        int fila = vista.listEmpleado.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ningún empleado seleccionado", "Selecciona un empleado");
            return;
        }

        Empleado empleado = (Empleado) vista.listEmpleado.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar \"" + empleado.getNombre() + "\"?", "Eliminar empleado");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.deleteEmpleado(empleado.getId());
            refrescarEmpleado();
            if (editando) {
                resetearEmpleado();
            }
            Util.mensajeInfo("Se ha eliminado \"" + empleado.getNombre() + "\"", "Empleado Eliminado");
        }
    }

    /***
     * Elimina un cliente
     */
    private void eliminarCliente() {
        int fila = vista.listCliente.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ningún cliente seleccionado", "Selecciona un cliente");
            return;
        }

        Cliente cliente = (Cliente) vista.listCliente.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar \"" + cliente.getNombre() + "\"?", "Eliminar cliente");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.deleteCliente(cliente.getId());
            refrescarCliente();
            if (editando) {
                resetearCliente();
            }
            Util.mensajeInfo("Se ha eliminado \"" + cliente.getNombre() + "\"", "Cliente Eliminado");
        }
    }

    /***
     * Elimina un proveedor
     */
    private void eliminarProveedor() {
        int fila = vista.listProveedor.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ningún proveedor seleccionado", "Selecciona un proveedor");
            return;
        }

        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar \"" + proveedor.getNombre() + "\"?", "Eliminar proveedor");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.deleteProveedor(proveedor.getId());
            refrescarProveedor();
            if (editando) {
                resetearProveedor();
            }
            Util.mensajeInfo("Se ha eliminado \"" + proveedor.getNombre() + "\"", "Proveedor Eliminado");
        }
    }

    /***
     * Elimina una venta
     */
    private void eliminarVenta() {
        int fila = vista.listVenta.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ninguna venta seleccionada", "Selecciona una venta");
            return;
        }

        Venta venta = (Venta) vista.listVenta.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar venta nº \"" + venta.getId() + "\"?", "Eliminar venta");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.deleteVenta(venta.getId());
            refrescarVenta();
            Util.mensajeInfo("Se ha eliminado venta nº \"" + venta.getId() + "\"", "Venta Eliminada");
        }
    }

    /***
     * Elimina una ventaProducto
     */
    private void eliminarVentaProducto() {
        int fila = vistaVenta.listVentaProducto.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ninguna línea seleccionada", "Selecciona una línea");
            return;
        }
        VentaProducto linea = (VentaProducto) vistaVenta.listVentaProducto.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar línea nº \"" + linea.getId() + "\"?", "Eliminar línea");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.deleteVentaProducto(linea.getId());
            refrescarVentaProducto(ventaActual);
            if (editando) {
                resetearVentaProducto(ventaActual);
            }
            Util.mensajeInfo("Se ha eliminado línea nº \"" + linea + "\"", "Línea Eliminada");
        }
        refrescarVentaProducto(ventaActual);
        refrescarVenta();
        modelo.generateVenta(ventaActual.getId());
    }
    // ELIMINAR FIN -----------------------------------------------------------------------------------------


    // BORRAR BBDD -----------------------------------------------------------------------------------------
    /***
     * Elimina la BBDD de Helados
     */
    private void borrarBBDDHelado() {
        if (modelo.getHeladosActivos().isEmpty()) {
            Util.mensajeInfo("No existen productos a borrar", "Borrar productos");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los productos?", "Borrar Productos");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.limpiarBBDDHelado();
            refrescarProducto();
        }
    }

    /***
     * Elimina la BBDD de empleados
     */
    private void borrarBBDDEmpleado() {
        if (modelo.getEmpleadosActivos().isEmpty()) {
            Util.mensajeInfo("No existen empleados a borrar", "Borrar Empleados");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los empleados?", "Borrar Empleados");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.limpiarBBDDEmpleado();
            refrescarEmpleado();
        }
    }

    /***
     * Elimina la BBDD de clientes
     */
    private void borrarBBDDCliente() {
        if (modelo.getEmpleadosActivos().isEmpty()) {
            Util.mensajeInfo("No existen clientes a borrar", "Borrar Clientes");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los clientes?", "Borrar Clientes");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.limpiarBBDDCliente();
            refrescarCliente();
        }
    }

    /***
     * Elimina la BBDD de proveedores
     */
    private void borrarBBDDProveedor() {
        if (modelo.getProveedoresActivos().isEmpty()) {
            Util.mensajeInfo("No existen proveedores a borrar", "Borrar Proveedores");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los proveedores?", "Borrar Proveedores");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.limpiarBBDDProveedor();
            refrescarProveedor();
        }
    }

    /***
     * Elimina la BBDD de Ventas
     */
    private void borrarBBDDVenta() {
        if (modelo.getVentas().isEmpty()) {
            Util.mensajeInfo("No existen ventas a borrar", "Borrar Ventas");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todas las ventas?", "Borrar Ventas");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.limpiarBBDDVenta();
            refrescarVenta();
        }
    }

    /***
     * Elimina la BBDD de VentaProductos
     */
    private void borrarBBDDVentaProducto() {
        if (modelo.getVentaProductos().isEmpty()) {
            Util.mensajeInfo("No existen productos a borrar", "Borrar detalle venta");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los productos?", "Borrar detalle venta");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.limpiarBBDDVentaProducto(ventaActual.getId());
            refrescarVentaProducto(ventaActual);
            modelo.generateVenta(ventaActual.getId());
        }
    }
    // BORRAR BBDD FIN -----------------------------------------------------------------------------------------


    // LISTENERS --------------------------------------------------------------------------------------------
    /***
     * Añadir los Window listener
     * @param listener
     */
    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
        vistaVenta.addWindowListener(listener);
    }

    /***
     * Añadir los listener
     * @param listener
     */
    private void addActionListeners(ActionListener listener) {
        // Limpiar
        vista.btnLimpiarProducto.addActionListener(listener);
        vista.btnLimpiarProducto.setActionCommand("limpiarProducto");
        vista.btnLimpiarEmpleado.addActionListener(listener);
        vista.btnLimpiarEmpleado.setActionCommand("limpiarEmpleado");
        vista.btnLimpiarCliente.addActionListener(listener);
        vista.btnLimpiarCliente.setActionCommand("limpiarCliente");
        vista.btnLimpiarProveedor.addActionListener(listener);
        vista.btnLimpiarProveedor.setActionCommand("limpiarProveedor");

        // Nuevo
        vista.btnNuevoProducto.addActionListener(listener);
        vista.btnNuevoProducto.setActionCommand("anadirProducto");
        vista.btnNuevoEmpleado.addActionListener(listener);
        vista.btnNuevoEmpleado.setActionCommand("anadirEmpleado");
        vista.btnNuevoCliente.addActionListener(listener);
        vista.btnNuevoCliente.setActionCommand("anadirCliente");
        vista.btnNuevoProveedor.addActionListener(listener);
        vista.btnNuevoProveedor.setActionCommand("anadirProveedor");
        vista.btnNuevoVenta.addActionListener(listener);
        vista.btnNuevoVenta.setActionCommand("anadirVenta");
        vistaVenta.btnAnnadir.addActionListener(listener);
        vistaVenta.btnAnnadir.setActionCommand("anadirVentaProducto");

        // Editar
        vista.btnEditaProducto.addActionListener(listener);
        vista.btnEditaProducto.setActionCommand("editarProducto");
        vista.btnEditarEmpleado.addActionListener(listener);
        vista.btnEditarEmpleado.setActionCommand("editarEmpleado");
        vista.btnEditarCliente.addActionListener(listener);
        vista.btnEditarCliente.setActionCommand("editarCliente");
        vista.btnEditarProveedor.addActionListener(listener);
        vista.btnEditarProveedor.setActionCommand("editarProveedor");
        vista.btnEditarVenta.addActionListener(listener);
        vista.btnEditarVenta.setActionCommand("editarVenta");
        vistaVenta.btnEditar.addActionListener(listener);
        vistaVenta.btnEditar.setActionCommand("editarVentaProducto");

        // Eliminar
        vista.btnEliminarProducto.addActionListener(listener);
        vista.btnEliminarProducto.setActionCommand("eliminarProducto");
        vista.btnEliminarEmpleado.addActionListener(listener);
        vista.btnEliminarEmpleado.setActionCommand("eliminarEmpleado");
        vista.btnEliminarCliente.addActionListener(listener);
        vista.btnEliminarCliente.setActionCommand("eliminarCliente");
        vista.btnEliminarProveedor.addActionListener(listener);
        vista.btnEliminarProveedor.setActionCommand("eliminarProveedor");
        vista.btnEliminarVenta.addActionListener(listener);
        vista.btnEliminarVenta.setActionCommand("eliminarVenta");
        vistaVenta.btnEliminar.addActionListener(listener);
        vistaVenta.btnEliminar.setActionCommand("eliminarVentaProducto");

        // Borrar BBDD
        vista.btnBorrarBBDDProducto.addActionListener(listener);
        vista.btnBorrarBBDDProducto.setActionCommand("borrarProducto");
        vista.btnBorrarBBDDEmpleado.addActionListener(listener);
        vista.btnBorrarBBDDEmpleado.setActionCommand("borrarEmpleado");
        vista.btnBorrarBBDDCliente.addActionListener(listener);
        vista.btnBorrarBBDDCliente.setActionCommand("borrarCliente");
        vista.btnBorrarBBDDProveedor.addActionListener(listener);
        vista.btnBorrarBBDDProveedor.setActionCommand("borrarProveedor");
        vista.btnBorrarBBDDVenta.addActionListener(listener);
        vista.btnBorrarBBDDVenta.setActionCommand("borrarVenta");
        vistaVenta.btnBorrarBBDDVentaProducto.addActionListener(listener);
        vistaVenta.btnBorrarBBDDVentaProducto.setActionCommand("borrarVentaProducto");

        // Guardar
        vista.btnGuardarProducto.addActionListener(listener);
        vista.btnGuardarProducto.setActionCommand("guardarProducto");
        vista.btnGuardarEmpleado.addActionListener(listener);
        vista.btnGuardarEmpleado.setActionCommand("guardarEmpleado");
        vista.btnGuardarCliente.addActionListener(listener);
        vista.btnGuardarCliente.setActionCommand("guardarCliente");
        vista.btnGuardarProveedor.addActionListener(listener);
        vista.btnGuardarProveedor.setActionCommand("guardarProveedor");
        vistaVenta.btnGuardar.addActionListener(listener);
        vistaVenta.btnGuardar.setActionCommand("guardarVentaProducto");

        // Cancelar
        vista.btnCancelarProducto.addActionListener(listener);
        vista.btnCancelarProducto.setActionCommand("cancelarProducto");
        vista.btnCancelarEmpleado.addActionListener(listener);
        vista.btnCancelarEmpleado.setActionCommand("cancelarEmpleado");
        vista.btnCancelarCliente.addActionListener(listener);
        vista.btnCancelarCliente.setActionCommand("cancelarCliente");
        vista.btnCancelarProveedor.addActionListener(listener);
        vista.btnCancelarProveedor.setActionCommand("cancelarProveedor");
        vistaVenta.btnCancelar.addActionListener(listener);
        vistaVenta.btnCancelar.setActionCommand("cancelarVentaProducto");

        // Mostrar
        vista.btnMostrarVentasEmpleado.addActionListener(listener);
        vista.btnMostrarVentasEmpleado.setActionCommand("mostrarVentasEmpleado");
        vista.btnMostrarVentasCliente.addActionListener(listener);
        vista.btnMostrarVentasCliente.setActionCommand("mostrarVentasCliente");
        vista.btnMostrarHeladosProveedor.addActionListener(listener);
        vista.btnMostrarHeladosProveedor.setActionCommand("mostrarHeladosProveedor");
        vista.btnMostrarDetalle.addActionListener(listener);
        vista.btnMostrarDetalle.setActionCommand("mostrarDetalle");

        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
    }
    // LISTENERS FIN --------------------------------------------------------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommmand = e.getActionCommand();
        CardLayout cl = (CardLayout) (vista.panelCard.getLayout());

        switch (actionCommmand) {
            case "Desconectar":
                if (conectado) {
                    modelo.desconectar();
                    vista.itemDesconectar.setText("Conectar");
                    vista.bloquearVistaExceptoMenu();
                    conectado = false;
                } else {
                    modelo.conectar();
                    vista.itemDesconectar.setText("Desconectar");
                    vista.desbloquearVista();
                    conectado = true;
                }
                break;
            case "Salir":
                System.exit(0);
                break;

            // PANEL CARD
            case "Helado":
                cl.show(vista.panelCard, "Helado");
                break;

            // LIMPIAR
            case "limpiarProducto":
                limpiarCamposProducto();
                limpiarCamposHelado();
                break;
            case "limpiarEmpleado":
                limpiarCamposEmpleado();
                break;
            case "limpiarCliente":
                limpiarCamposCliente();
                break;
            case "limpiarProveedor":
                limpiarCamposProveedor();
                break;

            // NUEVO
            case "anadirProducto":
                nuevoProducto();
                break;
            case "anadirEmpleado":
                nuevoEmpleado();
                break;
            case "anadirCliente":
                nuevoCliente();
                break;
            case "anadirProveedor":
                nuevoProveedor();
                break;
            case "anadirVenta":
                nuevoVenta();
                break;
            case "anadirVentaProducto":
                nuevoVentaProducto();
                break;

            // EDITAR
            case "editarProducto":
                editarProducto();
                break;
            case "editarEmpleado":
                editarEmpleado();
                break;
            case "editarCliente":
                editarCliente();
                break;
            case "editarProveedor":
                editarProveedor();
                break;
            case "editarVenta":
                editarVenta();
                break;
            case "editarVentaProducto":
                editarVentaProducto();
                break;

            // ELIMINAR
            case "eliminarProducto":
                eliminarHelado();
                break;
            case "eliminarEmpleado":
                eliminarEmpleado();
                break;
            case "eliminarCliente":
                eliminarCliente();
                break;
            case "eliminarProveedor":
                eliminarProveedor();
                break;
            case "eliminarVenta":
                eliminarVenta();
                break;
            case "eliminarVentaProducto":
                eliminarVentaProducto();
                break;

            // BORRAR BBDD
            case "borrarProducto":
                borrarBBDDHelado();
                break;
            case "borrarEmpleado":
                borrarBBDDEmpleado();
                break;
            case "borrarCliente":
                borrarBBDDCliente();
                break;
            case "borrarProveedor":
                borrarBBDDProveedor();
                break;
            case "borrarVenta":
                borrarBBDDVenta();
                break;
            case "borrarVentaProducto":
                borrarBBDDVentaProducto();
                break;

            // GUARDAR
            case "guardarProducto":
                guardarProducto();
                break;
            case "guardarEmpleado":
                guardarEmpleado();
                break;
            case "guardarCliente":
                guardarCliente();
                break;
            case "guardarProveedor":
                guardarProveedor();
                break;
            case "guardarVentaProducto":
                guardarVentaProducto();
                break;

            // CANCELAR
            case "cancelarProducto":
                resetearProducto();
                break;
            case "cancelarEmpleado":
                resetearEmpleado();
                break;
            case "cancelarCliente":
                resetearCliente();
                break;
            case "cancelarProveedor":
                resetearProveedor();
                break;
            case "cancelarVentaProducto":
                resetearVentaProducto(ventaActual);
                break;

            // MOSTRAR
            case "mostrarVentasEmpleado":
                listarVentasEmpleado();
                break;
            case "mostrarVentasCliente":
                listarVentasCliente();
                break;
            case "mostrarHeladosProveedor":
                listarHeladosProveedor();
                break;
            case "mostrarDetalle":
                listarDetalle();
                break;

            case "refrescarVenta":
                resetearVenta();
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Window ventana = e.getWindow();
        System.out.println(ventana);

        if (ventana == vista) {
            int resp = Util.mensajeConfirmación("¿Desea cerrar la vetana?", "Salir");
            if (resp == JOptionPane.OK_OPTION || resp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }

        if (ventana == vistaVenta) {
            refrescarVentaProducto(ventaActual);
            modelo.generateVenta(ventaActual.getId());
            resetearVenta();
            vistaVenta.dispose();
            Util.mensajeInfo("Se ha añadido una venta", "Venta - Producto");
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}

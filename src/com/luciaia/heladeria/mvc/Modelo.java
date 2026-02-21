package com.luciaia.heladeria.mvc;

import com.luciaia.heladeria.base.*;
import com.luciaia.heladeria.util.HibernateUtil;
import org.hibernate.query.Query;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;


public class Modelo {
    /***
     * Método conectar
     */
    public void conectar() {
        HibernateUtil.buildSessionFactory();
        HibernateUtil.openSession();
    }

    /***
     * Método desconectar
     */
    public void desconectar() {
        HibernateUtil.closeSessionFactory();
    }

    // OBTENER
    /***
     * Devuelve los clientes
     * @return lista de clientes
     */
    public List<Cliente> getClientesActivos() {
        return HibernateUtil.getCurrentSession().createQuery("FROM Cliente WHERE activo", Cliente.class).getResultList();
    }

    /***
     * Devuelve los empleados
     * @return lista de empleados
     */
    public List<Empleado> getEmpleadosActivos() {
        return HibernateUtil.getCurrentSession().createQuery("FROM Empleado WHERE activo", Empleado.class).getResultList();
    }

    /***
     * Devuelve los helados
     * @return lista de helados
     */
    public List<Helado> getHeladosActivos() {
        return HibernateUtil.getCurrentSession().createQuery("FROM Helado WHERE activo", Helado.class).getResultList();
    }

    /***
     * Devuelve los proveedores
     * @return lista de proveedores
     */
    public List<Proveedor> getProveedoresActivos() {
        return HibernateUtil.getCurrentSession().createQuery("FROM Proveedor WHERE activo", Proveedor.class).getResultList();
    }

    /***
     * Devuelve los ventas
     * @return lista de ventas
     */
    public List<Venta> getVentas() {
        return HibernateUtil.getCurrentSession().createQuery("FROM Venta").getResultList();
    }

    /***
     * Devuelve las líneas de venta
     * @return lista de líneas de venta
     */
    public List<VentaProducto> getVentaProductos() {
        return HibernateUtil.getCurrentSession().createQuery("FROM VentaProducto").getResultList();
    }
    /////////////////////////////////////////////////////////////////


    // OBTENER RELACIONADOS
    /***
     * Devuelve las ventas que tiene un empleado
     * @param empleado empleado
     * @return lista de ventas
     */
    public List<Venta> getVentasEmpleado(Empleado empleado) {
        return empleado.getVentas();
    }

    /***
     * Devuelve las ventas que tiene un cliente
     * @param cliente cliente
     * @return lista de ventas
     */
    public List<Venta> getVentasCliente(Cliente cliente) {
        return cliente.getCompras();
    }

    /***
     * Devuelve las lineas de ventas que tiene una venta
     * @param venta venta
     * @return lista de lineas de ventas
     */
    public List<VentaProducto> getLineasVentasVenta(Venta venta) {
        return venta.getLineasVenta();
    }

    /***
     * Devuelve los helados que tiene un proveedor
     * @param proveedor proveedor
     * @return lista de helados
     */
    public List<Helado> getHeladosProveedor(Proveedor proveedor) {
        return proveedor.getHelados();
    }
    /////////////////////////////////////////////////////////////////


    // INSERTAR
    /***
     * Insertar un objeto en la BBDD
     * @param object objeto a insertar en la BBDD
     */
    public void insert(Object object) {
        HibernateUtil.getCurrentSession().beginTransaction();
        HibernateUtil.getCurrentSession().save(object);
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }
    /////////////////////////////////////////////////////////////////


    // MODIFICAR
    /***
     * Modificar un objeto en la BBDD
     * @param object objeto a modificar en la BBDD
     */
    public void update(Object object) {
        HibernateUtil.getCurrentSession().beginTransaction();
        HibernateUtil.getCurrentSession().saveOrUpdate(object);
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }
    /////////////////////////////////////////////////////////////////


    // ELIMINAR
    /***
     * Eliminar un objeto en la BBDD
     * @param object objeto a eliminar en la BBDD
     */
    public void delete(Object object) {
        HibernateUtil.getCurrentSession().beginTransaction();
        HibernateUtil.getCurrentSession().delete(object);
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Eliminar un proveedor en la BBDD
     * @param idProveedor proveedor a eliminar en la BBDD
     */
    public void deleteProveedor(int idProveedor) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pEliminarProveedor");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idProveedor);
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Eliminar un empleado en la BBDD
     * @param idEmpleado empleado a eliminar en la BBDD
     */
    public void deleteEmpleado(int idEmpleado) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pEliminarEmpleado");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idEmpleado);
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Eliminar un cliente en la BBDD
     * @param idCliente cliente a eliminar en la BBDD
     */
    public void deleteCliente(int idCliente) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pEliminarCliente");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idCliente);
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Eliminar un helado en la BBDD
     * @param idHelado helado a eliminar en la BBDD
     */
    public void deleteHelado(int idHelado) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pEliminarHelado");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idHelado);
        query.execute();
    }

    /***
     * Eliminar una ventaProducto en la BBDD
     * @param idVentaProducto ventaProducto a eliminar en la BBDD
     */
    public void deleteVentaProducto(int idVentaProducto) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pEliminarVentaProducto");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVentaProducto);
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Eliminar una venta en la BBDD
     * @param idVenta venta a eliminar en la BBDD
     */
    public void deleteVenta(int idVenta) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pEliminarVenta");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVenta);
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Eliminar todos los objetos en la BBDD
     * @param claseEntidad clase a eliminar en la BBDD
     */
    public <T> void deleteAll(Class<T> claseEntidad) {
        HibernateUtil.getCurrentSession().beginTransaction();
        HibernateUtil.getCurrentSession().createQuery("DELETE FROM " + claseEntidad.getName()).executeUpdate();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }
    /////////////////////////////////////////////////////////////////


    // LIMPIAR
    /***
     * Limpiar la BBDD de proveedor
     */
    public void limpiarBBDDProveedor() {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pLimpiarProveedor");
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Limpiar la BBDD de empleado
     */
    public void limpiarBBDDEmpleado() {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pLimpiarEmpleado");
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Limpiar la BBDD de cliente
     */
    public void limpiarBBDDCliente() {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pLimpiarCliente");
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Limpiar la BBDD de helado
     */
    public void limpiarBBDDHelado() {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pLimpiarHelado");
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Limpiar la BBDD de ventaProducto
     */
    public void limpiarBBDDVentaProducto() {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pLimpiarVentaProducto");
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }

    /***
     * Limpiar la BBDD de venta
     */
    public void limpiarBBDDVenta() {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pLimpiarVenta");
        query.execute();
        HibernateUtil.getCurrentSession().getTransaction().commit();
    }
    /////////////////////////////////////////////////////////////////


    // EXISTE
    /***
     * Comprobar si existe un proveedor
     * @param nombreProveedor proveedor a buscar en la BBDD
     */
    public boolean proveedorExiste(String nombreProveedor) {
        HibernateUtil.getCurrentSession().beginTransaction();
        String hql = "SELECT count(p) FROM Proveedor p WHERE p.nombre = :nombre AND p.activo = true";
        int count = HibernateUtil.getCurrentSession().createQuery(hql, Integer.class)
                .setParameter("nombre", nombreProveedor)
                .uniqueResult();
        return count > 0;
    }

    /***
     * Comprobar si existe un empleado
     * @param emailEmpleado empleado a buscar en la BBDD
     */
    public boolean empleadoExiste(String emailEmpleado) {
        HibernateUtil.getCurrentSession().beginTransaction();
        String hql = "SELECT count(e) FROM Empleado e WHERE e.email = :email AND e.activo = true";
        int count = HibernateUtil.getCurrentSession().createQuery(hql, Integer.class)
                .setParameter("email", emailEmpleado)
                .uniqueResult();
        return count > 0;
    }

    /***
     * Comprobar si existe un cliente
     * @param emailCliente cliente a buscar en la BBDD
     */
    public boolean clienteExiste(String emailCliente) {
        HibernateUtil.getCurrentSession().beginTransaction();
        String hql = "SELECT count(c) FROM Cliente c WHERE c.email = :email AND c.activo = true";
        int count = HibernateUtil.getCurrentSession().createQuery(hql, Integer.class)
                .setParameter("email", emailCliente)
                .uniqueResult();
        return count > 0;
    }

    /***
     * Comprobar si existe un helado
     * @param nombreProducto helado a buscar en la BBDD
     */
    public boolean heladoExiste(String nombreProducto) {
        HibernateUtil.getCurrentSession().beginTransaction();
        String hql = "SELECT count(h) FROM Helado h WHERE h.nombre = :nombre AND h.activo = true";
        int count = HibernateUtil.getCurrentSession().createQuery(hql, Integer.class)
                .setParameter("nombre", nombreProducto)
                .uniqueResult();
        return count > 0;
    }
    /////////////////////////////////////////////////////////////////

    /***
     * Generar venta a partir de su detalle en la BBDD
     * @param idVenta venta a modificar en la BBDD
     */
    public void generateVenta(int idVenta) {
        StoredProcedureQuery query = HibernateUtil.getCurrentSession().createStoredProcedureCall("pGenerarVenta");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVenta);
        query.execute();
    }

    /***
     * Generar venta a partir de su detalle en la BBDD
     * @return id de la última venta
     */
    public int idUltimaVenta() {
        return HibernateUtil.getCurrentSession().createQuery("SELECT MAX(id) FROM Venta", Integer.class).getSingleResult();
    }
}

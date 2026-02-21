package com.luciaia.heladeria.mvc;

import com.luciaia.heladeria.base.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class Modelo {
    SessionFactory sessionFactory;

    /***
     * Método conectar
     */
    public void conectar() {
        ejecutarScript();
        Configuration configuracion = new Configuration();
        //Cargo el fichero Hibernate.cfg.xml
        configuracion.configure("hibernate.cfg.xml");

        //Indico la clase mapeada con anotaciones
        configuracion.addAnnotatedClass(Cliente.class);
        configuracion.addAnnotatedClass(Empleado.class);
        configuracion.addAnnotatedClass(Helado.class);
        configuracion.addAnnotatedClass(Proveedor.class);
        configuracion.addAnnotatedClass(Venta.class);
        configuracion.addAnnotatedClass(VentaProducto.class);

        //Creamos un objeto ServiceRegistry a partir de los parámetros de configuración
        //Esta clase se usa para gestionar y proveer de acceso a servicios
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(
                configuracion.getProperties()).build();

        //finalmente creamos un objeto sessionfactory a partir de la configuracion y del registro de servicios
        sessionFactory = configuracion.buildSessionFactory(ssr);
    }
    /*
    public void conectar() {
        HibernateUtil.buildSessionFactory();
        HibernateUtil.openSession();
    }
     */

    private void ejecutarScript() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/heladeria_3", "root", "");
        } catch (SQLException sqle) {
            try {
                // En caso de no tener la BBDD creada la crea por defecto
                conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");

                PreparedStatement statement = null;
                String code = leerFichero();
                String[] query = code.split("--");
                for (String aQuery : query) {
                    statement = conexion.prepareStatement(aQuery);
                    statement.executeUpdate();
                }
                assert statement != null;
                statement.close();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String leerFichero() throws IOException {
        // LIA
        System.out.println(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("scriptBBDD_Heladeria3.sql")
        );

        // Al utilizar Jar ya no existe como archivo físico, NO se puede utilizar FileReader
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("scriptBBDD_Heladeria3.sql");
        if (is == null) {
            throw new RuntimeException("No se encuentra el script SQL en resources");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //BufferedReader reader = new BufferedReader(new FileReader("scriptBBDD-Heladeria.sql"));
        String linea;
        StringBuilder stringBuilder = new StringBuilder();
        while ((linea = reader.readLine()) != null) {
            stringBuilder.append(linea);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /***
     * Método desconectar
     */
    public void desconectar() {
        //Cierro la factoria de sessiones
        if (sessionFactory != null && sessionFactory.isOpen())
            sessionFactory.close();
    }

    // OBTENER

    /***
     * Devuelve los clientes
     * @return lista de clientes
     */
    public ArrayList<Cliente> getClientesActivos() {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Cliente WHERE activo = true");
        ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) query.getResultList();
        sesion.close();
        return listaClientes;
    }

    /***
     * Devuelve los empleados
     * @return lista de empleados
     */
    public ArrayList<Empleado> getEmpleadosActivos() {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Empleado WHERE activo = true");
        ArrayList<Empleado> lista = (ArrayList<Empleado>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve los helados
     * @return lista de helados
     */
    public ArrayList<Helado> getHeladosActivos() {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Helado WHERE activo = true");
        ArrayList<Helado> lista = (ArrayList<Helado>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve los proveedores
     * @return lista de proveedores
     */
    public ArrayList<Proveedor> getProveedoresActivos() {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Proveedor WHERE activo = true");
        ArrayList<Proveedor> lista = (ArrayList<Proveedor>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve los ventas
     * @return lista de ventas
     */
    public ArrayList<Venta> getVentas() {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Venta");
        ArrayList<Venta> lista = (ArrayList<Venta>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve las líneas de venta
     * @return lista de líneas de venta
     */
    public ArrayList<VentaProducto> getVentaProductos() {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM VentaProducto");
        ArrayList<VentaProducto> lista = (ArrayList<VentaProducto>) query.getResultList();
        sesion.close();
        return lista;
    }
    /////////////////////////////////////////////////////////////////


    // OBTENER RELACIONADOS

    /***
     * Devuelve las ventas que tiene un empleado
     * @param empleado empleado
     * @return lista de ventas
     */
    public ArrayList<Venta> getVentasEmpleado(Empleado empleado) {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Venta WHERE empleado =: empleado");
        query.setParameter("empleado", empleado);
        ArrayList<Venta> lista = (ArrayList<Venta>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve las ventas que tiene un cliente
     * @param cliente cliente
     * @return lista de ventas
     */
    public ArrayList<Venta> getVentasCliente(Cliente cliente) {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Venta WHERE cliente =: cliente");
        query.setParameter("cliente", cliente);
        ArrayList<Venta> lista = (ArrayList<Venta>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve las lineas de ventas que tiene una venta
     * @param venta venta
     * @return lista de lineas de ventas
     */
    public ArrayList<VentaProducto> getLineasVentasVenta(Venta venta) {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM VentaProducto WHERE venta =: venta");
        query.setParameter("venta", venta);
        ArrayList<VentaProducto> lista = (ArrayList<VentaProducto>) query.getResultList();
        sesion.close();
        return lista;
    }

    /***
     * Devuelve los helados que tiene un proveedor
     * @param proveedor proveedor
     * @return lista de helados
     */
    public ArrayList<Helado> getHeladosProveedor(Proveedor proveedor) {
        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Helado WHERE proveedor =: proveedor");
        query.setParameter("proveedor", proveedor);
        ArrayList<Helado> lista = (ArrayList<Helado>) query.getResultList();
        sesion.close();
        return lista;
    }
    /////////////////////////////////////////////////////////////////


    // INSERTAR

    /***
     * Insertar un objeto en la BBDD
     * @param object objeto a insertar en la BBDD
     */
    public void insert(Object object) {
        //Obtengo una session a partir de la factoria de sesiones
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.save(object);
        sesion.getTransaction().commit();
        sesion.close();
    }
    /////////////////////////////////////////////////////////////////


    // MODIFICAR

    /***
     * Modificar un objeto en la BBDD
     * @param object objeto a modificar en la BBDD
     */
    public void update(Object object) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.saveOrUpdate(object);
        sesion.getTransaction().commit();
        sesion.close();
    }
    /////////////////////////////////////////////////////////////////


    // ELIMINAR

    /***
     * Eliminar un objeto en la BBDD
     * @param object objeto a eliminar en la BBDD
     */
    public void delete(Object object) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.delete(object);
        sesion.getTransaction().commit();
        sesion.close();
    }

    /***
     * Eliminar un proveedor en la BBDD
     * @param idProveedor proveedor a eliminar en la BBDD
     */
    public void deleteProveedor(int idProveedor) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureQuery("pEliminarProveedor");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idProveedor);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Eliminar un empleado en la BBDD
     * @param idEmpleado empleado a eliminar en la BBDD
     */
    public void deleteEmpleado(int idEmpleado) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureQuery("pEliminarEmpleado");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idEmpleado);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Eliminar un cliente en la BBDD
     * @param idCliente cliente a eliminar en la BBDD
     */
    public void deleteCliente(int idCliente) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureQuery("pEliminarCliente");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idCliente);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Eliminar un helado en la BBDD
     * @param idHelado helado a eliminar en la BBDD
     */
    public void deleteHelado(int idHelado) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureQuery("pEliminarHelado");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idHelado);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Eliminar una ventaProducto en la BBDD
     * @param idVentaProducto ventaProducto a eliminar en la BBDD
     */
    public void deleteVentaProducto(int idVentaProducto) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureQuery("pEliminarVentaProducto");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVentaProducto);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Eliminar una venta en la BBDD
     * @param idVenta venta a eliminar en la BBDD
     */
    public void deleteVenta(int idVenta) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureQuery("pEliminarVenta");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVenta);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Eliminar todos los objetos en la BBDD
     * @param claseEntidad clase a eliminar en la BBDD
     */
    public <T> void deleteAll(Class<T> claseEntidad) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.createQuery("DELETE FROM " + claseEntidad.getName()).executeUpdate();
        sesion.getTransaction().commit();
        sesion.close();
    }
    /////////////////////////////////////////////////////////////////


    // LIMPIAR

    /***
     * Limpiar la BBDD de proveedor
     */
    public void limpiarBBDDProveedor() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pLimpiarProveedor");
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Limpiar la BBDD de empleado
     */
    public void limpiarBBDDEmpleado() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pLimpiarEmpleado");
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Limpiar la BBDD de cliente
     */
    public void limpiarBBDDCliente() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pLimpiarCliente");
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Limpiar la BBDD de helado
     */
    public void limpiarBBDDHelado() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pLimpiarHelado");
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Limpiar la BBDD de ventaProducto
     */
    public void limpiarBBDDVentaProducto(int idVenta) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pLimpiarVentaProducto");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVenta);
        query.execute();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Limpiar la BBDD de venta
     */
    public void limpiarBBDDVenta() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pLimpiarVenta");
        query.execute();
        session.getTransaction().commit();
        session.close();
    }
    /////////////////////////////////////////////////////////////////


    // EXISTE

    /***
     * Comprobar si existe un proveedor
     * @param nombreProveedor proveedor a buscar en la BBDD
     */
    public boolean proveedorExiste(String nombreProveedor) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "SELECT count(p) FROM Proveedor p WHERE p.nombre = :nombre AND p.activo = true";
        Long count = session.createQuery(hql, Long.class).setParameter("nombre", nombreProveedor).uniqueResult();
        session.getTransaction().commit();
        return count != null && count > 0;
    }

    /***
     * Comprobar si existe un empleado
     * @param emailEmpleado empleado a buscar en la BBDD
     */
    public boolean empleadoExiste(String emailEmpleado) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "SELECT count(e) FROM Empleado e WHERE e.email = :email AND e.activo = true";
        Long count = session.createQuery(hql, Long.class).setParameter("email", emailEmpleado).uniqueResult();
        session.getTransaction().commit();
        return count != null && count > 0;
    }

    /***
     * Comprobar si existe un cliente
     * @param emailCliente cliente a buscar en la BBDD
     */
    public boolean clienteExiste(String emailCliente) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "SELECT count(c) FROM Cliente c WHERE c.email = :email AND c.activo = true";
        Long count = session.createQuery(hql, Long.class).setParameter("email", emailCliente).uniqueResult();
        session.getTransaction().commit();
        return count != null && count > 0;
    }

    /***
     * Comprobar si existe un helado
     * @param nombreProducto helado a buscar en la BBDD
     */
    public boolean heladoExiste(String nombreProducto) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "SELECT count(h) FROM Helado h WHERE h.nombre = :nombre AND h.activo = true";
        Long count = session.createQuery(hql, Long.class).setParameter("nombre", nombreProducto).uniqueResult();
        session.getTransaction().commit();
        return count != null && count > 0;
    }
    /////////////////////////////////////////////////////////////////

    /***
     * Generar venta a partir de su detalle en la BBDD
     * @param idVenta venta a modificar en la BBDD
     */
    public void generateVenta(int idVenta) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StoredProcedureQuery query = session.createStoredProcedureCall("pGenerarVenta");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, idVenta);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /***
     * Generar venta a partir de su detalle en la BBDD
     * @return id de la última venta
     */
    public Venta ultimaVenta() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Venta venta = session.createQuery("FROM Venta ORDER BY id DESC", Venta.class).setMaxResults(1).uniqueResult();
        session.getTransaction().commit();
        return venta;
    }
}

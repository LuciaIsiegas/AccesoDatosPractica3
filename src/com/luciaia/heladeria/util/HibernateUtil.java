package com.luciaia.heladeria.util;

import com.luciaia.heladeria.base.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static Session session;

    /**
     * Crea la factoria de sesiones
     */
    public static void buildSessionFactory() {

        Configuration configuration = new Configuration();
        // La llamada al método configure() carga los parámetros del fichero hibernate.cfg.xml
        configuration.configure();

        // Se registran las clases que hay que mapear con cada tabla de la base de datos
        configuration.addAnnotatedClass(Cliente.class);
        configuration.addAnnotatedClass(Empleado.class);
        configuration.addAnnotatedClass(Helado.class);
        configuration.addAnnotatedClass(Proveedor.class);
        configuration.addAnnotatedClass(Venta.class);
        configuration.addAnnotatedClass(VentaProducto.class);

        //Se crea una SessionFactory a partir del objeto Configuration
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * Abre una nueva sesión
     */
    public static void openSession() {
        session = sessionFactory.openSession();
    }

    /**
     * Devuelve la sesión actual
     *
     * @return
     */
    public static Session getCurrentSession() {
        if ((session == null) || (!session.isOpen()))
            openSession();
        return session;
    }

    /**
     * Cierra Hibernate
     */
    public static void closeSessionFactory() {
        if (session != null)
            session.close();
        if (sessionFactory != null)
            sessionFactory.close();
    }

    /***
     * Método leer fichero
     */
    private String leerFichero() throws IOException {
        // LIA
        System.out.println(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("scriptBBDD-Heladeria.sql")
        );

        // Al utilizar Jar ya no existe como archivo físico, NO se puede utilizar FileReader
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("scriptBBDD-Heladeria.sql");
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


}

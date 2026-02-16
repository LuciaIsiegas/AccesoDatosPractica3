package com.luciaia.heladeria.mvc;

import com.luciaia.heladeria.util.HibernateUtil;

public class Modelo {
    public void conectar() {
        HibernateUtil.buildSessionFactory();
        HibernateUtil.openSession();
    }

    public void desconectar(){
        HibernateUtil.closeSessionFactory();
    }



}

package com.luciaia.heladeria;

import com.luciaia.heladeria.mvc.Controlador;
import com.luciaia.heladeria.mvc.Modelo;
import com.luciaia.heladeria.mvc.Vista;

public class Principal {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
    }
}

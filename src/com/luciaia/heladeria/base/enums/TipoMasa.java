package com.luciaia.heladeria.base.enums;

public enum TipoMasa {
    BELGA("Belga"),
    AMERICANA("Americana"),
    LIEJA("Lieja"),
    ESPONJOSA("Esponjosa"),
    CRUJIENTE("Crujiente");

    private String valor;

    TipoMasa(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}

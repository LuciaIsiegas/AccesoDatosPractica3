package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cliente", schema = "heladeria_3", catalog = "")
public class Cliente {
    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private boolean activo;
    private List<Venta> compras;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "apellidos")
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Basic
    @Column(name = "activo")
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id &&
                activo == cliente.activo &&
                Objects.equals(nombre, cliente.nombre) &&
                Objects.equals(apellidos, cliente.apellidos) &&
                Objects.equals(email, cliente.email) &&
                Objects.equals(telefono, cliente.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellidos, email, telefono, activo);
    }

    @OneToMany(mappedBy = "cliente")
    public List<Venta> getCompras() {
        return compras;
    }

    public void setCompras(List<Venta> compras) {
        this.compras = compras;
    }
}

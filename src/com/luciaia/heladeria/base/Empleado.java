package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "empleado", schema = "heladeria_3", catalog = "")
public class Empleado {
    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private boolean activo;
    private List<Venta> ventas;

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
        Empleado empleado = (Empleado) o;
        return id == empleado.id &&
                activo == empleado.activo &&
                Objects.equals(nombre, empleado.nombre) &&
                Objects.equals(apellidos, empleado.apellidos) &&
                Objects.equals(email, empleado.email) &&
                Objects.equals(telefono, empleado.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellidos, email, telefono, activo);
    }

    @OneToMany(mappedBy = "empleado")
    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Apellidos: " + apellidos + " | Email: " + email + " | Telf: " + telefono + " | activo: " + activo;
    }
}

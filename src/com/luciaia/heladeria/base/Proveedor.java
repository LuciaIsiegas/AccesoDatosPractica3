package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "proveedor", schema = "heladeria_3", catalog = "")
public class Proveedor {
    private int id;
    private String nombre;
    private String personaContacto;
    private String email;
    private String telefono;
    private String direccion;
    private boolean activo;
    private List<Helado> helados;

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
    @Column(name = "persona_contacto")
    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
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
    @Column(name = "direccion")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        Proveedor proveedor = (Proveedor) o;
        return id == proveedor.id &&
                activo == proveedor.activo &&
                Objects.equals(nombre, proveedor.nombre) &&
                Objects.equals(personaContacto, proveedor.personaContacto) &&
                Objects.equals(email, proveedor.email) &&
                Objects.equals(telefono, proveedor.telefono) &&
                Objects.equals(direccion, proveedor.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, personaContacto, email, telefono, direccion, activo);
    }

    @OneToMany(mappedBy = "proveedor")
    public List<Helado> getHelados() {
        return helados;
    }

    public void setHelados(List<Helado> helados) {
        this.helados = helados;
    }
}

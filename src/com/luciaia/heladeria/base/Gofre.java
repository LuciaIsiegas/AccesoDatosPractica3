package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gofre", schema = "heladeria_3", catalog = "")
public class Gofre {
    private int id;
    private String nombre;
    private double precio;
    private Date fechaApertura;
    private Date fechaCaducidad;
    private String tipo;
    private boolean activo;
    private String topping;
    private boolean gluten;
    private String tipoMasa;
    private Proveedor proveedor;
    private List<VentaProducto> ventasGofres;

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
    @Column(name = "precio")
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Basic
    @Column(name = "fecha_apertura")
    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    @Basic
    @Column(name = "fecha_caducidad")
    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    @Basic
    @Column(name = "tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "activo")
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Basic
    @Column(name = "topping")
    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    @Basic
    @Column(name = "gluten")
    public boolean isGluten() {
        return gluten;
    }

    public void setGluten(boolean gluten) {
        this.gluten = gluten;
    }

    @Basic
    @Column(name = "tipo_masa")
    public String getTipoMasa() {
        return tipoMasa;
    }

    public void setTipoMasa(String tipoMasa) {
        this.tipoMasa = tipoMasa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gofre gofre = (Gofre) o;
        return id == gofre.id &&
                Double.compare(gofre.precio, precio) == 0 &&
                activo == gofre.activo &&
                gluten == gofre.gluten &&
                Objects.equals(nombre, gofre.nombre) &&
                Objects.equals(fechaApertura, gofre.fechaApertura) &&
                Objects.equals(fechaCaducidad, gofre.fechaCaducidad) &&
                Objects.equals(tipo, gofre.tipo) &&
                Objects.equals(topping, gofre.topping) &&
                Objects.equals(tipoMasa, gofre.tipoMasa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, fechaApertura, fechaCaducidad, tipo, activo, topping, gluten, tipoMasa);
    }

    @ManyToOne
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id", nullable = false)
    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @OneToMany(mappedBy = "gofre")
    public List<VentaProducto> getVentasGofres() {
        return ventasGofres;
    }

    public void setVentasGofres(List<VentaProducto> ventasGofres) {
        this.ventasGofres = ventasGofres;
    }
}

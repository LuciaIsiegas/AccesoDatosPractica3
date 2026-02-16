package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "helado", schema = "heladeria_3", catalog = "")
public class Helado {
    private int id;
    private String nombre;
    private double precio;
    private Date fechaApertura;
    private Date fechaCaducidad;
    private String tipo;
    private boolean activo;
    private String sabor;
    private boolean azucar;
    private double litros;
    private Proveedor proveedor;
    private List<VentaProducto> ventasHelados;

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
    @Column(name = "sabor")
    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    @Basic
    @Column(name = "azucar")
    public boolean isAzucar() {
        return azucar;
    }

    public void setAzucar(boolean azucar) {
        this.azucar = azucar;
    }

    @Basic
    @Column(name = "litros")
    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Helado helado = (Helado) o;
        return id == helado.id &&
                Double.compare(helado.precio, precio) == 0 &&
                activo == helado.activo &&
                azucar == helado.azucar &&
                Double.compare(helado.litros, litros) == 0 &&
                Objects.equals(nombre, helado.nombre) &&
                Objects.equals(fechaApertura, helado.fechaApertura) &&
                Objects.equals(fechaCaducidad, helado.fechaCaducidad) &&
                Objects.equals(tipo, helado.tipo) &&
                Objects.equals(sabor, helado.sabor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, fechaApertura, fechaCaducidad, tipo, activo, sabor, azucar, litros);
    }

    @ManyToOne
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id", nullable = false)
    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @OneToMany(mappedBy = "helado")
    public List<VentaProducto> getVentasHelados() {
        return ventasHelados;
    }

    public void setVentasHelados(List<VentaProducto> ventasHelados) {
        this.ventasHelados = ventasHelados;
    }
}

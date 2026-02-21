package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "venta_producto", schema = "heladeria_3", catalog = "")
public class VentaProducto {
    private int id;
    private int cantidad;
    private double precioTotal;
    private Helado helado;
    private Venta venta;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cantidad")
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "precio_total")
    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VentaProducto that = (VentaProducto) o;
        return id == that.id &&
                cantidad == that.cantidad &&
                Double.compare(that.precioTotal, precioTotal) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, precioTotal);
    }

    @ManyToOne
    @JoinColumn(name = "id_helado", referencedColumnName = "id")
    public Helado getHelado() {
        return helado;
    }

    public void setHelado(Helado helado) {
        this.helado = helado;
    }

    @ManyToOne
    @JoinColumn(name = "id_venta", referencedColumnName = "id", nullable = false)
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}

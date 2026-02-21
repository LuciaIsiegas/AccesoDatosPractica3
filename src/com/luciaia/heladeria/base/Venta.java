package com.luciaia.heladeria.base;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venta", schema = "heladeria_3", catalog = "")
public class Venta {
    private int id;
    private int cantidad;
    private double precioTotal;
    private Empleado empleado;
    private Cliente cliente;
    private List<VentaProducto> lineasVenta;

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
        Venta venta = (Venta) o;
        return id == venta.id &&
                cantidad == venta.cantidad &&
                Double.compare(venta.precioTotal, precioTotal) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, precioTotal);
    }

    @ManyToOne
    @JoinColumn(name = "id_empleado", referencedColumnName = "id", nullable = false)
    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @OneToMany(mappedBy = "venta")
    public List<VentaProducto> getLineasVenta() {
        return lineasVenta;
    }

    public void setLineasVenta(List<VentaProducto> lineasVenta) {
        this.lineasVenta = lineasVenta;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Cantidad: " + cantidad + " | Precio Total: " + precioTotal + " | Empleado: " + empleado.getNombre() + " | Cliente: " + cliente.getNombre();
    }
}

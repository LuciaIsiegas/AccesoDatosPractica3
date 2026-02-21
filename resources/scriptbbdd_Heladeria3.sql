create database if not exists heladeria_3;
--
USE heladeria_3;
--
CREATE TABLE IF NOT EXISTS proveedor (
                                         id int auto_increment primary key,
                                         nombre varchar(50) not null,
                                         persona_contacto varchar(50) not null,
                                         email varchar(100) not null,
                                         telefono varchar(9),
                                         direccion varchar(200),
                                         activo bool not null
);
--
CREATE TABLE IF NOT EXISTS empleado (
                                        id int auto_increment primary key,
                                        nombre varchar(50) not null,
                                        apellidos varchar(100),
                                        email varchar(100) not null,
                                        telefono varchar(9),
                                        activo bool not null
);
--
CREATE TABLE IF NOT EXISTS cliente (
                                       id int auto_increment primary key,
                                       nombre varchar(50) not null,
                                       apellidos varchar(100),
                                       email varchar(100) not null,
                                       telefono varchar(9),
                                       activo bool not null
);
--
CREATE TABLE IF NOT EXISTS helado (
                                      id int auto_increment primary key,
                                      nombre varchar(50) not null,
                                      precio float not null,
                                      fecha_apertura date,
                                      fecha_caducidad date not null,
                                      activo bool not null,
                                      sabor varchar(50) not null,
                                      azucar bool not null,
                                      litros float not null,
                                      id_proveedor int not null,
                                      foreign key(id_proveedor) references proveedor(id)
);
--
CREATE TABLE IF NOT EXISTS venta (
                                     id int auto_increment primary key,
                                     cantidad int,
                                     precio_total float,
                                     id_cliente int not null,
                                     id_empleado int not null,
                                     foreign key(id_cliente) references cliente(id),
                                     foreign key(id_empleado) references empleado(id)
);
--
CREATE TABLE IF NOT EXISTS venta_producto (
                                              id int auto_increment primary key,
                                              cantidad int not null,
                                              precio_total float,
                                              id_venta int not null,
                                              id_helado int not null,
                                              foreign key(id_venta) references venta(id),
                                              foreign key(id_helado) references helado(id)
);
--
drop procedure if exists pEliminarProveedor;
--
create procedure pEliminarProveedor(pid_proveedor int)
begin
    if exists (select id from proveedor where id = pid_proveedor) then
        if exists (select id from producto where id_proveedor = pid_proveedor) then
            update proveedor
            set activo = false
            where id = pid_proveedor;
        else
            delete from proveedor
            where id = pid_proveedor;
        end if;
    else
        select "El proveedor no existe";
    end if;
end;
--
drop procedure if exists pEliminarHelado;
--
create procedure pEliminarHelado(pid_helado int)
begin
    if exists (select id from helado where id = pid_helado) then
        if exists (select id from venta_producto where id_helado = pid_helado) then
            update helado
            set activo = false
            where id = pid_helado;
        else
            delete from helado
            where id = pid_helado;
        end if;
    else
        select "El helado no existe";
    end if;
end;
--
drop procedure if exists pEliminarCliente;
--
create procedure pEliminarCliente(pid_cliente int)
begin
    if exists (select id from cliente where id = pid_cliente) then
        if exists (select id from venta where id_cliente = pid_cliente) then
            update cliente
            set activo = false
            where id = pid_cliente;
        else
            delete from cliente
            where id = pid_cliente;
        end if;
    else
        select "El cliente no existe";
    end if;
end;
--
drop procedure if exists pEliminarEmpleado;
--
create procedure pEliminarEmpleado(pid_empleado int)
begin
    if exists (select id from empleado where id = pid_empleado) then
        if exists (select id from venta where id_empleado = pid_empleado) then
            update empleado
            set activo = false
            where id = pid_empleado;
        else
            delete from empleado
            where id = pid_empleado;
        end if;
    else
        select "El empleado no existe";
    end if;
end;
--
drop procedure if exists pEliminarVentaProducto;
--
create procedure pEliminarVentaProducto(pid_venta_producto int)
begin
    if exists (select id from venta_producto where id = pid_venta_producto) then
        delete from venta_producto
        where id = pid_venta_producto;
    else
        select "La venta no existe";
    end if;
end;
--
drop procedure if exists pEliminarVenta;
--
create procedure pEliminarVenta(pid_venta int)
begin
    if exists (select id from venta where id like pid_venta) then
        delete from venta_producto
        where id_venta = pid_venta;
        delete from venta
        where id = pid_venta;
    else
        select "La venta no existe";
    end if;
end;
--
drop procedure if exists pLimpiarProveedor;
--
create procedure pLimpiarProveedor()
begin
    UPDATE proveedor p
    SET activo = FALSE
    WHERE EXISTS (
                  SELECT id FROM helado h
                  WHERE h.id_proveedor = p.id
              );

    DELETE FROM proveedor
    WHERE id NOT IN (
        SELECT DISTINCT id_proveedor FROM helado
    );
end;
--
drop procedure if exists pLimpiarHelado;
--
create procedure pLimpiarHelado()
begin
    UPDATE helado h
    SET activo = FALSE
    WHERE EXISTS (
                  SELECT id FROM venta_producto vp
                  WHERE vp.id_helado = h.id
              );
    DELETE h FROM helado h
    WHERE NOT EXISTS (
            SELECT id
            FROM venta_producto vp
            WHERE vp.id_helado = h.id
        );
end;
--
drop procedure if exists pLimpiarCliente;
--
create procedure pLimpiarCliente()
begin
    UPDATE cliente c
    SET activo = FALSE
    WHERE EXISTS (
                  SELECT id FROM venta v
                  WHERE v.id_cliente = c.id
              );
    DELETE FROM cliente
    WHERE id NOT IN (SELECT DISTINCT id_cliente FROM venta);
end;
--
drop procedure if exists pLimpiarEmpleado;
--
create procedure pLimpiarEmpleado()
begin
    UPDATE empleado e
    SET activo = FALSE
    WHERE EXISTS (
                  SELECT id FROM venta v
                  WHERE v.id_empleado = e.id
              );
    DELETE FROM empleado
    WHERE id NOT IN (SELECT DISTINCT id_empleado FROM venta);
end;
--
drop procedure if exists pLimpiarVentaProducto;
--
create procedure pLimpiarVentaProducto(pid_venta int)
begin
    DELETE FROM venta_producto
    WHERE id_venta = pid_venta;
    DELETE FROM venta
    WHERE id = pid_venta;
end;
--
drop procedure if exists pLimpiarVenta;
--
create procedure pLimpiarVenta()
begin
    DELETE FROM venta_producto;
    DELETE FROM venta;
end;
--
drop procedure if exists pGenerarVenta;
--
create procedure pGenerarVenta(pid_venta int)
begin
    if exists (select id from venta where id = pid_venta) then
        update venta
        set cantidad = (select sum(cantidad) from venta_producto where id_venta = pid_venta),
            precio_total = (select sum(precio_total) from venta_producto where id_venta = pid_venta)
        where id = pid_venta;
    end if;
end;
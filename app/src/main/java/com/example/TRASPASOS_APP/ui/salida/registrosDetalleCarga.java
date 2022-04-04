package com.example.TRASPASOS_APP.ui.salida;

public class registrosDetalleCarga {

    private String numero_linea;
    private String codigoBarras;
    private String clave;
    private String cantidad;
    private String fecha_carga;
    private String id_produccion;
    private String no_tarima;
    private String fecha_produccion;
    private String no_lote;
    private String nombre_producto;
    private String uuid;

    public registrosDetalleCarga(String numero_linea, String codigoBarras, String clave, String cantidad, String fecha_carga, String id_produccion, String no_tarima, String fecha_produccion, String no_lote, String nombre_producto, String uuid) {
        this.numero_linea = numero_linea;
        this.codigoBarras = codigoBarras;
        this.clave = clave;
        this.cantidad = cantidad;
        this.fecha_carga = fecha_carga;
        this.id_produccion = id_produccion;
        this.no_tarima = no_tarima;
        this.fecha_produccion = fecha_produccion;
        this.no_lote = no_lote;
        this.nombre_producto = nombre_producto;
        this.uuid = uuid;
    }

    public String getNumero_linea() {
        return numero_linea;
    }

    public void setNumero_linea(String numero_linea) {
        this.numero_linea = numero_linea;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha_carga() {
        return fecha_carga;
    }

    public void setFecha_carga(String fecha_carga) {
        this.fecha_carga = fecha_carga;
    }

    public String getId_produccion() {
        return id_produccion;
    }

    public void setId_produccion(String id_produccion) {
        this.id_produccion = id_produccion;
    }

    public String getNo_tarima() {
        return no_tarima;
    }

    public void setNo_tarima(String no_tarima) {
        this.no_tarima = no_tarima;
    }

    public String getFecha_produccion() {
        return fecha_produccion;
    }

    public void setFecha_produccion(String fecha_produccion) {
        this.fecha_produccion = fecha_produccion;
    }

    public String getNo_lote() {
        return no_lote;
    }

    public void setNo_lote(String no_lote) {
        this.no_lote = no_lote;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

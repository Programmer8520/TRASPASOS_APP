package com.example.TRASPASOS_APP.ui.entrada;

public class datosTarimasEscaneadas {
    String no_linea, clave, fecha_carga, nombre_producto, etiqueta;

    public datosTarimasEscaneadas(String no_linea, String clave, String fecha_carga, String nombre_producto, String etiqueta) {
        this.no_linea = no_linea;
        this.clave = clave;
        this.fecha_carga = fecha_carga;
        this.nombre_producto = nombre_producto;
        this.etiqueta = etiqueta;
    }

    public String getNo_linea() {
        return no_linea;
    }

    public void setNo_linea(String no_linea) {
        this.no_linea = no_linea;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFecha_carga() {
        return fecha_carga;
    }

    public void setFecha_carga(String fecha_carga) {
        this.fecha_carga = fecha_carga;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}

package com.example.TRASPASOS_APP.ui.salida;

public class resumen {

    String posicion, nombreProducto, cantidad;

    public resumen(String posicion, String nombreProducto, String cantidad) {
        this.posicion = posicion;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
    }

    public String getPosicion() {
        return posicion;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getCantidad() {
        return cantidad;
    }

}

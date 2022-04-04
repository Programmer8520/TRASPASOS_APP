package com.example.TRASPASOS_APP.ui.salida;

public class objDestino {

    String cortina, bodega_destino_nombre, Destino,contador;

    public objDestino(String cortina, String bodega_destino_nombre, String Destino, String contador) {
        this.cortina = cortina;
        this.bodega_destino_nombre = bodega_destino_nombre;
        this.Destino = Destino;
        this.contador=contador;

    }

    public String getCortina() {
        return cortina;
    }

    public String getBodega_destino_nombre() {
        return bodega_destino_nombre;
    }

    public String getDestino() {
        return Destino;
    }

    public String getContador() {
        return contador;
    }
}

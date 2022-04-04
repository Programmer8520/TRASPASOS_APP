package com.example.TRASPASOS_APP.ui.salida;

public class producto {

    String idEtiqueta, nombreProducto, codigoBarras, noLote, noTarimaDetalle, cantidadPorTarima, nPartidaSAP, position, fechayHora;

    public producto(String idEtiqueta, String nombreProducto, String noTarimaDetalle, String fechayHora, String position) {
        this.idEtiqueta = idEtiqueta;
        this.nombreProducto = nombreProducto;
        this.codigoBarras = codigoBarras;
        this.noLote = noLote;
        this.noTarimaDetalle = noTarimaDetalle;
        this.cantidadPorTarima = cantidadPorTarima;
        this.nPartidaSAP = nPartidaSAP;
        this.position = position;
        this.fechayHora = fechayHora;
    }

    public String getIdEtiqueta() {
        return idEtiqueta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getNoTarimaDetalle() {
        return noTarimaDetalle;
    }

    public String getFechayHora() {
        return fechayHora;
    }

    public String getPosition() { return position; }


}

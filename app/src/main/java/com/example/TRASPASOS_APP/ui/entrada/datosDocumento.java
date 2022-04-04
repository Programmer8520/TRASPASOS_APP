package com.example.TRASPASOS_APP.ui.entrada;

public class datosDocumento {
    String bodega_origen_nombre, bodega_destino_nombre, nombre_transportista, numero_documento, numeroTarimas, conductor, salida_rampa;
    String nombre_analista, nombre_montacarguista, nombre_guardia, uuid, observaciones, idregistrado;

    public datosDocumento(String idregistrado, String uuid, String bodega_origen_nombre, String bodega_destino_nombre, String nombre_transportista, String numero_documento, String numeroTarimas, String conductor, String salida_rampa, String nombre_analista, String nombre_montacarguista, String nombre_guardia, String observaciones){
        this.idregistrado = idregistrado;
        this.uuid = uuid;
        this.bodega_origen_nombre = bodega_origen_nombre;
        this.bodega_destino_nombre = bodega_destino_nombre;
        this.nombre_transportista = nombre_transportista;
        this.numero_documento = numero_documento;
        this.numeroTarimas = numeroTarimas;
        this.conductor = conductor;
        this.salida_rampa = salida_rampa;
        this.nombre_analista = nombre_analista;
        this.nombre_montacarguista = nombre_montacarguista;
        this.nombre_guardia = nombre_guardia;
        this.observaciones = observaciones;
    }

    public String getIdregistrado() { return idregistrado; }

    public String getUuid() { return uuid; }

    public String getBodega_origen_nombre() { return bodega_origen_nombre; }

    public String getBodega_destino_nombre() {
        return bodega_destino_nombre;
    }

    public String getNombre_transportista() {
        return nombre_transportista;
    }

    public String getNumero_documento() { return numero_documento; }

    public String getNumeroTarimas() { return numeroTarimas; }

    public String getConductor() {
        return conductor;
    }

    public String getSalida_rampa() {
        return salida_rampa;
    }

    public String getNombre_analista() {
        return nombre_analista;
    }

    public String getNombre_montacarguista() {
        return nombre_montacarguista;
    }

    public String getNombre_guardia() {
        return nombre_guardia;
    }

    public String getObservaciones() { return observaciones; }
}

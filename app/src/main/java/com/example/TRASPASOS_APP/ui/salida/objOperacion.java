package com.example.TRASPASOS_APP.ui.salida;

public class objOperacion {

    String tiempo_de_operacion,
            Documento_SAP,
            nombre_montacarguista,
            nombre_analista_inventarios,
            nombre_guardia_seguridad,
            conductor,
            placas_tractor,
            placas_remolque,
            entrada_rampa,
            salida_rampa,
            numero;

    public objOperacion(String tiempo_de_operacion,
                        String Documento_SAP,
                        String nombre_montacarguista,
                        String nombre_analista_inventarios,
                        String nombre_guardia_seguridad,
                        String conductor,
                        String placas_tractor,
                        String placas_remolque,
                        String entrada_rampa,
                        String salida_rampa,
                        String numero
                         ) {

        this.tiempo_de_operacion = tiempo_de_operacion;
        this.Documento_SAP = Documento_SAP;
        this.nombre_montacarguista = nombre_montacarguista;
        this.nombre_analista_inventarios = nombre_analista_inventarios;
        this.nombre_guardia_seguridad = nombre_guardia_seguridad;
        this.conductor = conductor;
        this.placas_tractor = placas_tractor;
        this.placas_remolque = placas_remolque;
        this.entrada_rampa = entrada_rampa;
        this.salida_rampa = salida_rampa;
        this.numero = numero;

    }
}

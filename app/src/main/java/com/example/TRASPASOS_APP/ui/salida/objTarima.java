package com.example.TRASPASOS_APP.ui.salida;

public class objTarima {

    String numero_documento,
            numero_linea,
            nombre_producto,
            codigo_ean_upc,
            clave,
            cantidad,
            fecha_carga,
            id_produccion,
            no_tarima,
            fecha_produccion,
            no_lote,
            consecutivo;

    public objTarima(String numero_documento,
                     String numero_linea,
                     String nombre_producto,
                     String codigo_ean_upc,
                     String clave,
                     String cantidad,
                     String fecha_carga,
                     String id_produccion,
                     String no_tarima,
                     String fecha_produccion,
                     String no_lote,
                     String consecutivo
                    ) {

        this.numero_documento = numero_documento;
        this.numero_linea = numero_linea;
        this.nombre_producto = nombre_producto;
        this.codigo_ean_upc = codigo_ean_upc;
        this.clave = clave;
        this.cantidad = cantidad;
        this.fecha_carga = fecha_carga;
        this.id_produccion = id_produccion;
        this.no_tarima = no_tarima;
        this.fecha_produccion = fecha_produccion;
        this.no_lote = no_lote;
        this.consecutivo = consecutivo;


    }
}

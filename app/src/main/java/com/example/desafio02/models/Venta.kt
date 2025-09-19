package com.example.desafio02.models

data class Venta(
    var id: String = "",
    var clienteId: String = "",
    var listaProductos: Map<String, Int> = mapOf(), // idProducto -> cantidad
    var total: Double = 0.0,
    var fecha: String = ""
)
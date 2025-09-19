package com.example.desafio02

data class Producto (
    var id: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var precio: Double = 0.0,
    var imagenUrl: String = "",
    var stock: Int = 0
    )
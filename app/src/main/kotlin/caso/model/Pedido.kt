package model

import model.*

data class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<ProductoPedido>,
    var total: Double,
    var estado: EstadoPedido,
    var metodoPago: MetodoPago,
    val fecha: String
)

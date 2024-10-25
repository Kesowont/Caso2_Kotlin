package model

import model.*

data class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<ProductoPedido>,
    var total: Double,
    var estado: EstadoPedido,
    var pagos: MutableList<Pago> = mutableListOf(),
    val fecha: String
){
    // Método para agregar un pago
    fun agregarPago(pago: Pago) {
        pagos.add(pago)
        if (montoTotalPagado() >= total) {
            estado = EstadoPedido.PAGADO
        }
    }

    // Método para calcular el monto total pagado
    fun montoTotalPagado(): Double {
        return pagos.sumOf { it.monto }
    }
}

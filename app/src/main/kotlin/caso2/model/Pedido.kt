package model

import java.time.LocalDateTime

class Pedido(
    private val idPedido: Int,
    private val cliente: Cliente,
    private val productos: MutableList<ProductoPedido> = mutableListOf(),
    private var estado: EstadoPedido = EstadoPedido.PENDIENTE,
    private val pagos: MutableList<Pago> = mutableListOf(),
    private val fecha: LocalDateTime = LocalDateTime.now()
) {
    enum class EstadoPedido {
        PENDIENTE, PAGADO, PROCESANDO, ENVIADO, ENTREGADO
    }

    private var total: Float = 0f
    private var disponible: Boolean = true

    init {
        calcularTotal()
        verificarDisponibilidad()
    }

    fun getIdPedido(): Int = idPedido
    fun getCliente(): Cliente = cliente
    fun getProductos(): List<ProductoPedido> = productos
    fun getEstado(): EstadoPedido = estado
    fun getPagos(): List<Pago> = pagos
    fun getFecha(): LocalDateTime = fecha
    fun getTotal(): Float = total
    fun isDisponible(): Boolean = disponible

    fun agregarProducto(productoPedido: ProductoPedido) {
        productos.add(productoPedido)
        calcularTotal()
        verificarDisponibilidad()
    }

    fun agregarPago(pago: Pago) {
        pagos.add(pago)
        actualizarEstadoPago()
    }

    private fun calcularTotal() {
        total = productos.sumOf { it.calcularSubtotal().toDouble() }.toFloat()
    }

    private fun verificarDisponibilidad() {
        disponible = productos.all { it.verificarDisponibilidad() }
    }

    private fun actualizarEstadoPago() {
        val totalPagado = pagos.sumOf { it.getMonto().toDouble() }
        if (totalPagado >= total) {
            estado = EstadoPedido.PAGADO
            // Actualizar existencias de los productos
            productos.forEach { productoPedido ->
                productoPedido.getProducto().actualizarExistencias(-productoPedido.getCantidad())
            }
        }
    }

    fun actualizarEstado(nuevoEstado: EstadoPedido) {
        estado = nuevoEstado
    }
    // Método para generar un resumen del pedido
    fun generarResumen(): String {
        return """
            Pedido #$idPedido
            Cliente: ${cliente.getNombre()}
            Fecha: $fecha
            Estado: $estado
            Productos:
            ${productos.joinToString("\n") { "- ${it .toString()}" }}
            Total: $total
            Disponible: $disponible
            Pagos realizados: ${pagos.size}
        """.trimIndent()
    }

    override fun toString(): String {
        return generarResumen()
    }
}
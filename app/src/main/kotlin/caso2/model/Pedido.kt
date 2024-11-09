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

    fun eliminarProducto(productoPedido: ProductoPedido){
        productos.remove(productoPedido)
        calcularTotal()
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

    fun actualizarEstado() {
        val siguienteEstado = obtenerSiguienteEstado()
        if (siguienteEstado == null) {
            println("El pedido ya está en su estado final: $estado. No se puede avanzar a un estado posterior.")
            return
        }

        if (estado == EstadoPedido.PENDIENTE && getMontoFaltante() > 0) {
            println("El pedido no está pagado en su totalidad. No se puede actualizar el estado.")
            return
        }

        estado = siguienteEstado
        println("Estado del pedido actualizado a: $estado")
    }

    fun obtenerSiguienteEstado(): EstadoPedido? {
        return when (estado) {
            EstadoPedido.PENDIENTE -> EstadoPedido.PAGADO
            EstadoPedido.PAGADO -> EstadoPedido.PROCESANDO
            EstadoPedido.PROCESANDO -> EstadoPedido.ENVIADO
            EstadoPedido.ENVIADO -> EstadoPedido.ENTREGADO
            EstadoPedido.ENTREGADO -> null // No hay estado siguiente después de "ENTREGADO"
        }
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

    fun getTotalPagado(): Float {
        return pagos.sumOf { it.getMonto().toDouble() }.toFloat()
    }

    fun getMontoFaltante(): Float {
        return total - getTotalPagado()
    }

}
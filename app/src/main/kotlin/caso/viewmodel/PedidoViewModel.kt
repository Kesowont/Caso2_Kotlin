package viewmodel

import model.*

class PedidoViewModel {
    private val pedidos = mutableListOf<Pedido>()

    init {
        cargarPedidos()
    }

    private fun cargarPedidos() {
        val productoA = Producto("Galletas Animalotes", 3.0, 0.18, 50)
        val productoB = Producto("Broaster", 10.0, 0.10, 30)
        val productoC = Producto("Inca Cola Personal 3L", 8.0, 0.05, 20)

        val cliente1 = Cliente(1, "Ivan Sulca", "SJL Cerrito", "982256192")
        val cliente2 = Cliente(2, "Uwu", "Olivos Auki", "999666333")

        val pedido1 = Pedido(
            id = 1,
            cliente = cliente1,
            productos = listOf(
                ProductoPedido(productoA, 2),
                ProductoPedido(productoB, 1)
            ),
            total = calcularTotal(listOf(
                ProductoPedido(productoA, 2),
                ProductoPedido(productoB, 1)
            )),
            estado = EstadoPedido.PENDIENTE,
            metodoPago = MetodoPago.Efectivo("PEN"),
            fecha = "2024-10-16"
        )

        val pedido2 = Pedido(
            id = 2,
            cliente = cliente2,
            productos = listOf(
                ProductoPedido(productoC, 3),
                ProductoPedido(productoA, 1)
            ),
            total = calcularTotal(listOf(
                ProductoPedido(productoC, 3),
                ProductoPedido(productoA, 1)
            )),
            estado = EstadoPedido.PROCESANDO,
            metodoPago = MetodoPago.TarjetaCredito("9876543210987654", "11/25", "Visa"),
            fecha = "2024-10-18"
        )

        pedidos.add(pedido1)
        pedidos.add(pedido2)
    }

    fun actualizarEstadoPedido(pedidoId: Int, nuevoEstado: EstadoPedido) {
        pedidos.find { it.id == pedidoId }?.estado = nuevoEstado
    }

    fun agregarProductoAPedido(pedidoId: Int, productoPedido: ProductoPedido) {
        val pedido = pedidos.find { it.id == pedidoId }
        pedido?.let {
            it.productos.toMutableList().add(productoPedido)
            productoPedido.producto.reducirExistencias(productoPedido.cantidad)
            it.total = calcularTotal(it.productos)
        }
    }

    fun cambiarMetodoPago(pedidoId: Int, nuevoMetodoPago: MetodoPago) {
        pedidos.find { it.id == pedidoId }?.metodoPago = nuevoMetodoPago
    }

    private fun calcularTotal(productos: List<ProductoPedido>): Double {
        return productos.sumOf { it.producto.calcularPrecioConImpuesto() * it.cantidad }
    }

    // Función mejorada para mostrar pedidos de forma legible
    fun mostrarPedidos() {
        pedidos.forEach { pedido ->
            println("---------------------------------------------------")
            println("Pedido ID: ${pedido.id}")
            println("Cliente: ${pedido.cliente.nombre} - ${pedido.cliente.direccion}")
            println("Estado: ${pedido.estado}")
            println("Total: ${"%.2f".format(pedido.total)} USD")
            println("Método de Pago: ${pedido.metodoPago}")
            println("Productos:")
            pedido.productos.forEach { productoPedido ->
                println("  - ${productoPedido.producto.nombre}: ${productoPedido.cantidad} unidades (Precio Unitario: ${"%.2f".format(productoPedido.producto.calcularPrecioConImpuesto())} USD)")
            }
            println("Fecha: ${pedido.fecha}")
            println("---------------------------------------------------")
        }
    }
}

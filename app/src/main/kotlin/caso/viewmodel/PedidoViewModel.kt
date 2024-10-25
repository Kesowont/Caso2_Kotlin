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
            //metodoPago = MetodoPago.Efectivo("PEN"),
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
            //metodoPago = MetodoPago.TarjetaCredito("9876543210987654", "11/25", "Visa"),
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

    private fun calcularTotal(productos: List<ProductoPedido>): Double {
        return productos.sumOf { it.producto.calcularPrecioConImpuesto() * it.cantidad }
    }

    // Nueva función para registrar un pago en un pedido
    fun registrarPago(pedidoId: Int, pago: Pago) {
        val pedido = pedidos.find { it.id == pedidoId }
        if (pedido != null) {
            pedido.agregarPago(pago)
            println("Se ha registrado un pago de ${pago.monto} para el pedido ${pedido.id}.")
            if (pedido.estado == EstadoPedido.PAGADO) {
                println("El pedido ${pedido.id} ha sido pagado en su totalidad y su estado es ahora PAGADO.")
            }
        } else {
            println("No se encontró el pedido con ID $pedidoId.")
        }
    }

       // Nueva función para verificar existencias
    fun verificarExistenciasAntesDeConfirmar(pedido: Pedido): Boolean {
        return pedido.productos.all { it.producto.existencias >= it.cantidad }
    }

    fun confirmarPedido(pedidoId: Int): Boolean {
        val pedido = pedidos.find { it.id == pedidoId }
        if (pedido != null) {
            if (verificarExistenciasAntesDeConfirmar(pedido)) {
                // Si hay suficientes existencias, reducimos las existencias
                pedido.productos.forEach { it.producto.reducirExistencias(it.cantidad) }
                pedido.estado = EstadoPedido.PROCESANDO
                println("El pedido ${pedido.id} ha sido confirmado y está en proceso.")
                return true
            } else {
                println("El pedido ${pedido.id} no se puede confirmar. Hay productos con existencias insuficientes.")
                return false
            }
        }
        return false
    }

    // Nueva función para agregar pedidos
    fun agregarPedido(nuevoPedido: Pedido): Boolean {
        if (verificarExistenciasAntesDeConfirmar(nuevoPedido)) {
            // Reducir existencias de los productos en el pedido
            nuevoPedido.productos.forEach { it.producto.reducirExistencias(it.cantidad) }
            // Calcular y actualizar el total del pedido
            nuevoPedido.total = calcularTotal(nuevoPedido.productos)
            // Agregar el pedido a la lista de pedidos y cambiar el estado a PROCESANDO
            nuevoPedido.estado = EstadoPedido.PENDIENTE
            pedidos.add(nuevoPedido)
            println("El pedido ${nuevoPedido.id} ha sido agregado y está en proceso.")
            return true
        } else {
            println("El pedido ${nuevoPedido.id} no se puede agregar. Hay productos con existencias insuficientes.")
            return false
        }
    }

    fun mostrarPedidos() {
        pedidos.forEach { pedido ->
            println("---------------------------------------------------")
            println("Pedido ID: ${pedido.id}")
            println("Cliente: ${pedido.cliente.nombre} - ${pedido.cliente.direccion}")
            println("Estado: ${pedido.estado}")
            println("Total: ${"%.2f".format(pedido.total)} USD")
            println("Total Pagado: ${"%.2f".format(pedido.montoTotalPagado())} USD")
            println("Método(s) de Pago:")
            pedido.pagos.forEach { pago ->
                println("  - ${pago.monto} USD a través de ${pago.metodoPago}")
            }
            println("Productos:")
            pedido.productos.forEach { productoPedido ->
                println("  - ${productoPedido.producto.nombre}: ${productoPedido.cantidad} unidades (Precio Unitario: ${"%.2f".format(productoPedido.producto.calcularPrecioConImpuesto())} USD)")
            }
            println("Fecha: ${pedido.fecha}")
            println("---------------------------------------------------")
        }
    }
}

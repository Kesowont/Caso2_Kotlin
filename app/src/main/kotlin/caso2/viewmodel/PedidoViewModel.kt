package viewmodel

import model.*
import java.time.LocalDateTime

class PedidoViewModel {
    private val pedidos: MutableList<Pedido> = mutableListOf()
    private val clientes: MutableList<Cliente> = mutableListOf()
    private val productos: MutableList<Producto> = mutableListOf()

    fun crearCliente(id: Int, nombre: String, direccion: String, telefono: String): Cliente {
        val cliente = Cliente(id, nombre, direccion, telefono)
        clientes.add(cliente)
        return cliente
    }

    fun crearProducto(id: Int, nombre: String, precio: Float, impuesto: Float, existencias: Int): Producto {
        val producto = Producto(id, nombre, precio, impuesto, existencias)
        productos.add(producto)
        return producto
    }

    fun crearPedido(cliente: Cliente): Pedido {
        val nuevoPedido = Pedido(pedidos.size + 1, cliente, fecha = LocalDateTime.now())
        pedidos.add(nuevoPedido)
        return nuevoPedido
    }

    fun eliminarPedido(pedido: Pedido) {
        pedidos.remove(pedido)
    }

    fun eliminarProductoDePedido(pedido: Pedido, productoPedido: ProductoPedido) {
        pedido.eliminarProducto(productoPedido)
    }

    fun agregarProductoAPedido(pedido: Pedido, producto: Producto, cantidad: Int) {
        val productoPedido = ProductoPedido(producto, cantidad)
        pedido.agregarProducto(productoPedido)
    }

    fun procesarPago(pedido: Pedido, pago: Pago) {
        pedido.agregarPago(pago)
    }

    fun crearPagoEfectivo(idPago: Int, monto: Float, moneda: String = "PEN"): Efectivo {
        return Efectivo(idPago, monto, moneda)
    }

    fun crearPagoCheque(idPago: Int, monto: Float, nombre: String, entidadBancaria: String): Cheque {
        return Cheque(idPago, monto, nombre, entidadBancaria)
    }

    fun crearPagoTarjeta(idPago: Int, monto: Float, numero: String, fechaCaducidad: String, tipo: String, cuotas: Int, interes: Float = 0.0f): TarjetaCredito {
        return TarjetaCredito(idPago, monto, numero, fechaCaducidad, tipo, cuotas, interes)
    }

    fun actualizarEstadoPedidoManual(pedido: Pedido) {
        pedido.actualizarEstado()
    }

    fun obtenerPedidos(): List<Pedido> = pedidos
    fun obtenerClientes(): List<Cliente> = clientes
    fun obtenerProductos(): List<Producto> = productos
}
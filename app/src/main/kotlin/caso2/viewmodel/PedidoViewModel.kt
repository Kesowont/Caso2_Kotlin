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

    fun agregarProductoAPedido(pedido: Pedido, producto: Producto, cantidad: Int) {
        val productoPedido = ProductoPedido(producto, cantidad)
        pedido.agregarProducto(productoPedido)
    }

    fun procesarPago(pedido: Pedido, pago: Pago) {
        pedido.agregarPago(pago)
    }

    fun actualizarEstadoPedido(pedido: Pedido, nuevoEstado: Pedido.EstadoPedido) {
        pedido.actualizarEstado(nuevoEstado)
    }

    fun obtenerPedidos(): List<Pedido> = pedidos
    fun obtenerClientes(): List<Cliente> = clientes
    fun obtenerProductos(): List<Producto> = productos
}
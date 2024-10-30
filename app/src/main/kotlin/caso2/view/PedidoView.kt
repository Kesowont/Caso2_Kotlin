package view

import model.*
import viewmodel.PedidoViewModel

class PedidoView(private val viewModel: PedidoViewModel) {
    fun mostrarMenuPrincipal() {
        println("\n--- SISTEMA DE PEDIDOS ---")
        println("1. Crear Cliente")
        println("2. Crear Producto")
        println("3. Crear Pedido")
        println("4. Ver Pedidos")
        println("5. Salir")
        print("Seleccione una opción: ")
    }

    fun crearCliente() {
        print("ID del cliente: ")
        val id = readLine()?.toIntOrNull() ?: return
        print("Nombre del cliente: ")
        val nombre = readLine() ?: return
        print("Dirección del cliente: ")
        val direccion = readLine() ?: return
        print("Teléfono del cliente: ")
        val telefono = readLine() ?: return

        val cliente = viewModel.crearCliente(id, nombre, direccion, telefono)
        println("Cliente creado: $cliente")
    }

    fun crearProducto() {
        print("ID del producto: ")
        val id = readLine()?.toIntOrNull() ?: return
        print("Nombre del producto: ")
        val nombre = readLine() ?: return
        print("Precio del producto: ")
        val precio = readLine()?.toFloatOrNull() ?: return
        print("Impuesto del producto: ")
        val impuesto = readLine()?.toFloatOrNull() ?: return
        print("Existencias del producto: ")
        val existencias = readLine()?.toIntOrNull() ?: return

        val producto = viewModel.crearProducto(id, nombre, precio, impuesto, existencias)
        println("Producto creado: $producto")
    }

    fun crearPedido() {
        val clientes = viewModel.obtenerClientes()
        if (clientes.isEmpty()) {
            println("No hay clientes registrados. Por favor, cree un cliente primero.")
            return
        }

        println("Clientes disponibles:")
        clientes.forEachIndexed { index, cliente ->
            println("${index + 1}. ${cliente.getNombre()}")
        }
        print("Seleccione el número del cliente: ")
        val clienteIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (clienteIndex !in clientes.indices) {
            println("Selección inválida.")
            return
        }

        val pedido = viewModel.crearPedido(clientes[clienteIndex])
        println("Pedido creado para ${clientes[clienteIndex].getNombre()}")

        while (true) {
            println("\nProductos disponibles:")
            val productos = viewModel.obtenerProductos()
            productos.forEachIndexed { index, producto ->
                println("${index + 1}. ${producto.getNombre()} - Precio: ${producto.getPrecio()} - Existencias: ${producto.getExistencias()}")
            }
            print("Seleccione el número del producto (0 para terminar): ")
            val productoIndex = readLine()?.toIntOrNull()?.minus(1) ?: continue
            if (productoIndex == -1) break
            if (productoIndex !in productos.indices) {
                println("Selección inválida.")
                continue
            }

            print("Cantidad: ")
            val cantidad = readLine()?.toIntOrNull() ?: continue

            viewModel.agregarProductoAPedido(pedido, productos[productoIndex], cantidad)
            println("Producto agregado al pedido.")
        }

        println("Pedido finalizado: $pedido")
    }

    fun mostrarPedidos() {
        val pedidos = viewModel.obtenerPedidos()
        if (pedidos.isEmpty()) {
            println("No hay pedidos registrados.")
            return
        }

        println("\n--- LISTA DE PEDIDOS ---")
        pedidos.forEach { pedido ->
            println(pedido)
            println("------------------------")
        }
    }
}
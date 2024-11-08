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
        println("5. Realizar Pago")
        println("6. Salir")
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
            println("Total Pagado: ${pedido.getTotalPagado()}")
            println("Monto Faltante: ${pedido.getMontoFaltante()}")
            println("------------------------")
        }
    }

    fun realizarPago() {
        val pedidos = viewModel.obtenerPedidos()
        if (pedidos.isEmpty()) {
            println("No hay pedidos registrados.")
            return
        }

        println("\nPedidos disponibles para pago:")
        pedidos.forEachIndexed { index, pedido ->
            println("${index + 1}. Pedido #${pedido.getIdPedido()} - Estado: ${pedido.getEstado()} - Total: ${pedido.getTotal()}")
            println("Total Pagado: ${pedido.getTotalPagado()}")
            println("Monto Faltante: ${pedido.getMontoFaltante()}")
        }
        print("Seleccione el número del pedido: ")
        val pedidoIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (pedidoIndex !in pedidos.indices) {
            println("Selección inválida.")
            return
        }

        val pedido = pedidos[pedidoIndex]
        if (pedido.getEstado() == Pedido.EstadoPedido.PAGADO) {
            println("El pedido ya está pagado.")
            return
        }

        println("Seleccione el método de pago:")
        println("1. Efectivo")
        println("2. Cheque")
        println("3. Tarjeta de Crédito")
        val metodoPago = readLine()?.toIntOrNull() ?: return

        val pago = when (metodoPago) {
            1 -> {
                print("ID del pago: ")
                val idPago = readLine()?.toIntOrNull() ?: return
                print("Monto del pago: ")
                val monto = readLine()?.toFloatOrNull() ?: return
                print("Moneda (ej. PEN, USD): ")
                val moneda = readLine() ?: "PEN"
                viewModel.crearPagoEfectivo(idPago, monto, moneda)
            }
            2 -> {
                print("ID del pago: ")
                val idPago = readLine()?.toIntOrNull() ?: return
                print("Monto del pago: ")
                val monto = readLine()?.toFloatOrNull() ?: return
                print("Nombre del titular del cheque: ")
                val nombre = readLine() ?: return
                print("Entidad bancaria: ")
                val entidadBancaria = readLine() ?: return
                viewModel.crearPagoCheque(idPago, monto, nombre, entidadBancaria)
            }
            3 -> {
                print("ID del pago: ")
                val idPago = readLine()?.toIntOrNull() ?: return
                print("Monto del pago: ")
                val monto = readLine()?.toFloatOrNull() ?: return
                print("Número de la tarjeta: ")
                val numero = readLine() ?: return
                print("Fecha de caducidad (MM/AA): ")
                val fechaCaducidad = readLine() ?: return
                print("Tipo de tarjeta (Visa, Mastercard, etc.): ")
                val tipo = readLine() ?: return
                print("Número de cuotas: ")
                val cuotas = readLine()?.toIntOrNull() ?: 1
                print("Interés (en porcentaje, ej. 0.05 para 5%): ")
                val interes = readLine()?.toFloatOrNull() ?: 0.0f
                viewModel.crearPagoTarjeta(idPago, monto, numero, fechaCaducidad, tipo, cuotas, interes)
            }
            else -> {
                println("Método de pago inválido.")
                return
            }
        }

        viewModel.procesarPago(pedido, pago)
        println("Pago realizado. Estado del pedido actualizado.")
        println("Total Pagado: ${pedido.getTotalPagado()}")
        println("Monto Faltante: ${pedido.getMontoFaltante()}")
    }

}
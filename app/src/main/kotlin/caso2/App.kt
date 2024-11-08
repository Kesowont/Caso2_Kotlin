package caso2

import view.PedidoView
import viewmodel.PedidoViewModel

class App {
    private val viewModel = PedidoViewModel()
    private val view = PedidoView(viewModel)

    fun run() {
        while (true) {
            view.mostrarMenuPrincipal()
            when (readLine()) {
                "1" -> view.crearCliente()
                "2" -> view.crearProducto()
                "3" -> view.crearPedido()
                "4" -> view.mostrarPedidos()
                "5" -> view.realizarPago()
                "6" -> {
                    println("¡Hasta luego!")
                    return
                }
                else -> println("Opción no válida. Intente de nuevo.")
            }
        }
    }
}

fun main() {
    App().run()
}
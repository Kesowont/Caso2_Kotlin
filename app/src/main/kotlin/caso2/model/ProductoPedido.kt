package model

data class ProductoPedido(
    private val producto: Producto,
    private val cantidad: Int
) {
    fun getProducto(): Producto = producto
    fun getCantidad(): Int = cantidad

    fun calcularSubtotal(): Float {
        return producto.calcularPrecioConImpuesto() * cantidad
    }

    fun verificarDisponibilidad(): Boolean {
        return cantidad <= producto.getExistencias()
    }

    override fun toString(): String {
        return "ProductoPedido(producto=${producto.getNombre()}, cantidad=$cantidad)"
    }
}
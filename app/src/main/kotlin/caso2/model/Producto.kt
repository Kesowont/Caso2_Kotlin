package model

data class Producto(
    private val idProducto: Int,
    private val nombre: String,
    private var precio: Float,
    private val impuesto: Float,
    private var existencias: Int
) {
    fun getIdProducto(): Int = idProducto
    fun getNombre(): String = nombre
    fun getPrecio(): Float = precio
    fun getImpuesto(): Float = impuesto
    fun getExistencias(): Int = existencias

    fun actualizarExistencias(cantidad: Int) {
        existencias += cantidad
    }

    fun calcularPrecioConImpuesto(): Float {
        return precio + (precio * impuesto)
    }

    override fun toString(): String {
        return "Producto(id=$idProducto, nombre='$nombre', precio=$precio, impuesto=$impuesto, existencias=$existencias)"
    }
}
package model

data class Producto(
    val nombre: String,
    val precio: Double,
    val impuesto: Double, // Porcentaje de impuesto aplicado (por ejemplo, 0.18 para 18%)
    var existencias: Int // Cantidad de producto en stock
) {
    fun calcularPrecioConImpuesto(): Double {
        val precioBase = precio
        return precioBase + (precioBase * impuesto)
    }

    fun reducirExistencias(cantidadPedida: Int) {
        if (existencias >= cantidadPedida) {
            existencias -= cantidadPedida
        } else {
            throw IllegalArgumentException("No hay suficientes existencias para el producto: $nombre")
        }
    }
}

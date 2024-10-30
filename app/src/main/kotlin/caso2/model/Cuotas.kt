package model

class Cuotas(
    private val idCuotas: Int,
    public var estado: String = "pendiente",
    private val aporte: Float
) {
    fun getIdCuotas(): Int {
        return idCuotas
    }

    fun getEstado(): String {
        return estado
    }

    fun getAporte(): Float {
        return aporte
    }

    fun detallesCuotas(): String {
        return "ID Cuotas: $idCuotas, Estado: $estado, Aporte: $aporte"
    }

    fun actualizarEstado(nuevoEstado: String) {
        estado = nuevoEstado
    }
}
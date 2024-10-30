package model

open class Pago(
    private val idPago: Int,
    private val monto: Float
) {
    fun getIdPago(): Int {
        return idPago
    }

    fun getMonto(): Float {
        return monto
    }

    open fun detallesPago(): String {
        return "ID Pago: $idPago, Monto: $monto"
    }
}
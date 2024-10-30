package model

class Efectivo(
    idPago: Int,
    monto: Float,
    private val moneda: String = "PEN"
) : Pago(idPago, monto) {

    fun getMoneda(): String {
        return moneda
    }

    override fun detallesPago(): String {
        return super.detallesPago() + ", Moneda: $moneda"
    }
}
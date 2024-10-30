package model

class Cheque(
    idPago: Int,
    monto: Float,
    private val nombre: String,
    private val entidadBancaria: String
) : Pago(idPago, monto) {

    fun getNombre(): String {
        return nombre
    }

    fun getEntidadBancaria(): String {
        return entidadBancaria
    }

    override fun detallesPago(): String {
        return super.detallesPago() + ", Nombre: $nombre, Entidad Bancaria: $entidadBancaria"
    }
}
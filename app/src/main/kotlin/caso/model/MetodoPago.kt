package model

sealed class MetodoPago {
    data class TarjetaCredito(val numero: String, val fechaCaducidad: String, val tipo: String) : MetodoPago()
    data class Efectivo(val moneda: String) : MetodoPago()
    data class Cheque(val nombre: String, val entidadBancaria: String) : MetodoPago()
}

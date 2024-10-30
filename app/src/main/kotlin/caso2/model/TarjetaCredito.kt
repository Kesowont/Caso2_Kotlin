package model

class TarjetaCredito(
    idPago: Int,
    monto: Float,
    private val numero: String,
    private val fechaCaducidad: String,
    private val tipo: String,
    private val cuotas: Int,
    private val interes: Float = 0.0f // Interés por defecto es 0.0
) : Pago(idPago, monto) {

    private val cuotasList: MutableList<Cuotas> = mutableListOf()

    init {
        var aux: Int = 0
        // Calcula el valor del aporte por cuota con o sin interés
        val aporte = if (cuotas > 1) {
            monto * (1 + interes) / cuotas // Calcula con interés si hay más de una cuota
        } else {
            monto / cuotas
        }

        // Crea las cuotas con el valor del aporte
        repeat(cuotas) {
            aux+=1;
            cuotasList.add(Cuotas(aux, "pendiente", aporte))
        }
    }

    fun getNumero(): String {
        return numero
    }

    fun getFechaCaducidad(): String {
        return fechaCaducidad
    }

    fun getTipo(): String {
        return tipo
    }

    fun getCuotas(): List<Cuotas> {
        return cuotasList
    }

    fun marcarCuotaComoPagada(numeroCuota: Int) {
        if (numeroCuota in 0 until cuotasList.size) {
            cuotasList[numeroCuota].estado = "completo"
        }
    }

    override fun detallesPago(): String {
        return super.detallesPago() + ", Número: $numero, Fecha de Caducidad: $fechaCaducidad, Tipo: $tipo, Cuotas: ${cuotasList.joinToString { it.detallesCuotas() }}"
    }
}
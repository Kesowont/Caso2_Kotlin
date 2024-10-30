package model

data class Cliente(
    private val idCliente: Int,
    private val nombre: String,
    private val direccion: String,
    private val telefono: String
) {
    fun getIdCliente(): Int = idCliente
    fun getNombre(): String = nombre
    fun getDireccion(): String = direccion
    fun getTelefono(): String = telefono

    override fun toString(): String {
        return "Cliente(id=$idCliente, nombre='$nombre', direccion='$direccion', telefono='$telefono')"
    }
}
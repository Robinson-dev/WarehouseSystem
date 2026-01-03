package cl.arriagada.foodexpress.models

sealed class EstadoPedido {
    object Pendiente : EstadoPedido()
    object EnPreparacion : EstadoPedido()
    object ListoParaEntrega : EstadoPedido()

    data class Error(val mensaje: String) : EstadoPedido()
}
// EstadoOrden.kt
package com.example.myappcafe.EstadoOrden
// 'sealed class' es como un enum pero con superpoderes
// una orden SOLO puede estar en uno de estos estados
sealed class EstadoOrden {
    // 'object' significa que solo hay UNA instancia de este estado
    object Recibida : EstadoOrden()
    object Preparando : EstadoOrden()
    object Lista : EstadoOrden()
    // 'data class' puede llevar informacion extra
    data class Error(val mensaje: String) : EstadoOrden()
}
// por que sealed class y no enum?
// porque Error necesita un mensaje personalizado
// con enum no podrias hacer eso
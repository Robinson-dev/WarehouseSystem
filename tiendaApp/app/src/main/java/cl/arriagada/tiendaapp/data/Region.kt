package cl.arriagada.tiendaapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer


/**
 * Data class que representa una región de Chile
 * Serializable permite convertir esta clase desde/hacia JSON
 * @param id Identificador único de la región
 * @param nombre Nombre de la región
 */
@Serializable
data class Region(
    val id: Int,
    val nombre: String
)
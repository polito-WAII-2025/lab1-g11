package g11.models

import kotlinx.serialization.Serializable

@Serializable
data class MostFrequentedArea(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val entriesCount: Int
)

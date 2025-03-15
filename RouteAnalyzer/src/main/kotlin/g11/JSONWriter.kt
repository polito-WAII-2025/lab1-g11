package g11  // Ensure this matches your package name

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class OutputData(
    val maxDistanceFromStart: MaxDistance,
    val mostFrequentedArea: MostFrequentedArea? = null,
    val waypointsOutsideGeofence: GeofenceResult
)

@Serializable
data class MaxDistance(val waypoint: Waypoint, val distanceKm: Double)

@Serializable
data class MostFrequentedArea(val centralWaypoint: Waypoint, val areaRadiusKm: Double, val entriesCount: Int)

@Serializable
data class GeofenceResult(val count: Int, val waypoints: List<Waypoint>)

fun writeOutput(filePath: String, data: OutputData) {
    File(filePath).writeText(Json.encodeToString(data))
}

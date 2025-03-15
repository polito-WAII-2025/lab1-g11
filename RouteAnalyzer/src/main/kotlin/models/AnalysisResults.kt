package g11.models

import kotlinx.serialization.Serializable

@Serializable
data class AnalysisResults(
    val maxDistanceFromStart: MaxDistanceFromStart?,
    val mostFrequentedArea: MostFrequentedArea?,
    val waypointsOutsideGeofence: WaypointsOutsideGeofence?
)
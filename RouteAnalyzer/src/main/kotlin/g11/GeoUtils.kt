package g11

import kotlin.math.*

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double, earthRadiusKm: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    return 2 * earthRadiusKm * atan2(sqrt(a), sqrt(1 - a))
}

fun maxDistanceFromStart(waypoints: List<Waypoint>, earthRadiusKm: Double): Pair<Waypoint, Double> {
    val start = waypoints.firstOrNull() ?: throw IllegalArgumentException("No waypoints found")
    return waypoints.maxByOrNull { haversine(start.latitude, start.longitude, it.latitude, it.longitude, earthRadiusKm) }
        ?.let { it to haversine(start.latitude, start.longitude, it.latitude, it.longitude, earthRadiusKm) }
        ?: throw IllegalArgumentException("No valid waypoints for distance calculation")
}

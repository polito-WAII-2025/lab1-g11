package g11

fun isOutsideGeofence(
    waypoint: Waypoint,
    centerLat: Double,
    centerLon: Double,
    radiusKm: Double,
    earthRadiusKm: Double
): Boolean {
    val distance = haversine(waypoint.latitude, waypoint.longitude, centerLat, centerLon, earthRadiusKm)
    return distance > radiusKm
}

fun waypointsOutsideGeofence(waypoints: List<Waypoint>, config: Config): List<Waypoint> {
    return waypoints.filter { isOutsideGeofence(it, config.geofenceCenterLatitude, config.geofenceCenterLongitude, config.geofenceRadiusKm, config.earthRadiusKm) }
}

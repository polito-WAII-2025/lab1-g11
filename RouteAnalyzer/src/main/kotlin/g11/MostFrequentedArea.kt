package g11

fun findMostFrequentedArea(waypoints: List<Waypoint>, config: Config): Pair<Waypoint, Int>? {
    val radius = config.mostFrequentedAreaRadiusKm ?: (waypoints.size / 10.0).coerceAtLeast(0.1) // Default radius if not set

    val groupedAreas = waypoints.groupBy { wp ->
        waypoints.count { haversine(wp.latitude, wp.longitude, it.latitude, it.longitude, config.earthRadiusKm) <= radius }
    }

    return groupedAreas.maxByOrNull { it.value.size }?.let { it.value.first() to it.value.size }
}

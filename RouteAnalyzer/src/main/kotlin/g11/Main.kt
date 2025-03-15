package g11  // Make sure this matches your package name

fun main() {
    println("RouteAnalyzer started...")

    // Load configuration from YAML
    val config = readConfig("custom-parameters.yml")

    // Read waypoints from CSV
    val waypoints = readWaypoints("waypoints.csv")

    // Compute max distance from start
    val (farthestWaypoint, maxDistance) = maxDistanceFromStart(waypoints, config.earthRadiusKm)

    // Compute most frequented area
    val mostFrequentedAreaResult = findMostFrequentedArea(waypoints, config)

    // Find waypoints outside geofence
    val outsideGeofence = waypointsOutsideGeofence(waypoints, config)

    // Create output object
    val output = OutputData(
        maxDistanceFromStart = MaxDistance(farthestWaypoint, maxDistance),
        mostFrequentedArea = mostFrequentedAreaResult?.let {
            MostFrequentedArea(it.first, config.mostFrequentedAreaRadiusKm ?: 0.5, it.second)
        },
        waypointsOutsideGeofence = GeofenceResult(outsideGeofence.size, outsideGeofence)
    )

    // Write output to JSON
    writeOutput("output.json", output)
    println("Analysis complete! Output saved to output.json")
}

package g11.services

import g11.models.AnalysisResults
import g11.models.CustomParameters
import g11.utils.*


fun analyzeRoute(waypointsCsvPath: String, yamlFilePath: String): AnalysisResults? {
    val waypoints = readWaypointsFromCsv(waypointsCsvPath)
    CustomParameters.load(yamlFilePath) // Load parameters globally


    // If mostFrequentedAreaRadiusKm is null, calculate it using waypoints
    if (CustomParameters.mostFrequentedAreaRadiusKm == null) {
        CustomParameters.mostFrequentedAreaRadiusKm = calculateMostFrequentedAreaRadiusKm(waypoints)
    }

    // Perform the analysis
    return AnalysisResults(
        maxDistanceFromStart = calculateMaxDistanceFromStart(waypoints),
        mostFrequentedArea = calculateMostFrequentedArea(waypoints, CustomParameters.mostFrequentedAreaRadiusKm!!),
        waypointsOutsideGeofence = countWaypointsOutsideGeoFence(
            waypoints,
            CustomParameters.geofenceCenterLatitude,
            CustomParameters.geofenceCenterLongitude,
            CustomParameters.geofenceRadiusKm
        )
    )
}


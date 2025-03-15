package g11.services

import g11.utils.RouteAnalyzer
import g11.models.AnalysisResults
import g11.utils.*


class RouteAnalysisService {

    fun analyzeRoute(waypointsCsvPath: String, yamlFilePath: String): AnalysisResults? {
        val waypoints = readWaypointsFromCsv(waypointsCsvPath)
        val customParams = loadCustomParameters(yamlFilePath) ?: return null

        val routeAnalyzer = RouteAnalyzer(customParams.earthRadiusKm)

        // If mostFrequentedAreaRadiusKm is null, calculate it using waypoints
        if (customParams.mostFrequentedAreaRadiusKm == null) {
            customParams.mostFrequentedAreaRadiusKm = routeAnalyzer.calculateMostFrequentedAreaRadiusKm(waypoints)
        }

        // Perform the analysis
        return AnalysisResults(
            maxDistanceFromStart = routeAnalyzer.calculateMaxDistanceFromStart(waypoints),
            mostFrequentedArea = routeAnalyzer.calculateMostFrequentedArea(waypoints, customParams.mostFrequentedAreaRadiusKm!!),
            waypointsOutsideGeofence = routeAnalyzer.countWaypointsOutsideGeoFence(
                waypoints,
                customParams.geofenceCenterLatitude,
                customParams.geofenceCenterLongitude,
                customParams.geofenceRadiusKm
            )
        )
    }
}

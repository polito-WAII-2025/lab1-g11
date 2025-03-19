package g11.services

import g11.models.AdvancedAnalysisResults
import g11.models.AnalysisResults
import g11.models.CustomParameters
import g11.models.Waypoint
import g11.utils.*

// Perform the analysis
fun performAnalysis(waypoints: List<Waypoint>): AnalysisResults {
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

// Perform advanced analysis
fun performAdvancedAnalysis(waypoints: List<Waypoint>): AdvancedAnalysisResults {
    return AdvancedAnalysisResults(
        totalDistance = calculateTotalDistance(waypoints)
    );
}



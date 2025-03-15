package g11

import g11.models.AnalysisResults
import g11.utils.*


fun main() {

    // Read the waypoints from the CSV file
    val waypoints = readWaypointsFromCsv("evaluation/waypoints.csv")

    // Load the custom parameters from the YAML file
    val customParams = loadCustomParametersFromYaml("evaluation/custom-parameters.yml" , waypoints)


    val maxDistanceFromStart = calculateMaxDistanceFromStart(waypoints)
    val mostFrequentedArea = calculateMostFrequentedArea(waypoints, customParams!!.mostFrequentedAreaRadiusKm)
    val waypointsOutsideGeofence = countWaypointsOutsideGeoFence(
        waypoints,
        customParams.geofenceCenterLatitude,
        customParams.geofenceCenterLongitude,
        customParams.geofenceRadiusKm
    )

    val results = AnalysisResults(
        maxDistanceFromStart,
        mostFrequentedArea,
        waypointsOutsideGeofence
    )

    writeResultsToJsonFile(results, "evaluation/output.json")
}


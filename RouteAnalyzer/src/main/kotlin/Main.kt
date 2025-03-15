package g11

import g11.utils.*

import java.io.File

fun main(args: Array<String>) {
    // Get file paths from command-line arguments (or use defaults)
    val waypointsCsvPath = args.getOrNull(0) ?: "evaluation/waypoints.csv"
    val yamlFilePath = args.getOrNull(1) ?: "evaluation/custom-parameters.yml"

    // Ensure the files exist before proceeding
    if (!File(waypointsCsvPath).exists()) {
        println("Error: Waypoints CSV file not found at '$waypointsCsvPath'")
        return
    }
    if (!File(yamlFilePath).exists()) {
        println("Error: YAML configuration file not found at '$yamlFilePath'")
        return
    }

    // Read the waypoints from the CSV file
    val waypoints = readWaypointsFromCsv(waypointsCsvPath)

    // Load the custom parameters from the YAML file
    val customParams = loadCustomParametersFromYaml(yamlFilePath, waypoints)
        ?: run {
            println("Error: Failed to load parameters from '$yamlFilePath'")
            return
        }

    println(customParams)

    println("max distance from start")
    println(calculateMaxDistanceFromStart(waypoints))

    println("most frequented area")
    println(calculateMostFrequentedArea(waypoints, customParams!!.mostFrequentedAreaRadiusKm))

    println("out of geofence")
    println(countWaypointsOutsideGeoFence(waypoints, customParams.geoFence))

}
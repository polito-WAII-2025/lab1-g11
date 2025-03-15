package g11

import g11.utils.*


fun main() {

    // Read the waypoints from the CSV file
    val waypoints = readWaypointsFromCsv("evaluation/waypoints.csv")


    // Load the custom parameters from the YAML file
    val customParams = loadCustomParametersFromYaml("evaluation/custom-parameters.yml" , waypoints)

    println(customParams)

    println("max distance from start")
    println(calculateMaxDistanceFromStart(waypoints))

    println("most frequented area")
    println(calculateMostFrequentedArea(waypoints, customParams!!.mostFrequentedAreaRadiusKm))

    println("out of geofence")
    println(countWaypointsOutsideGeoFence(waypoints, customParams.geofenceCenterLatitude, customParams.geofenceCenterLongitude, customParams.geofenceRadiusKm))

}
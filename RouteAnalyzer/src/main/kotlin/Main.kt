package g11

import g11.models.CustomParameters
import g11.services.*
import g11.utils.calculateMostFrequentedAreaRadiusKm
import g11.utils.readWaypointsFromCsv
import g11.utils.validateFiles
import g11.utils.writeResultsToJsonFile


fun main(args: Array<String>) {

    val argMap = args
        .mapNotNull { it.split("=").takeIf { parts -> parts.size == 2 }?.let { it[0] to it[1] } }
        .toMap()

    val waypointsCsvPath = argMap["--waypoints"] ?: "evaluation/waypoints.csv"
    val yamlFilePath = argMap["--customparams"] ?: "evaluation/custom-parameters.yml"

    if (!validateFiles(waypointsCsvPath, yamlFilePath)) return


    val waypoints = readWaypointsFromCsv(waypointsCsvPath)

    // Load parameters globally
    CustomParameters.load(yamlFilePath)
    // Calculate the most frequented area radius if not provided
    if (CustomParameters.mostFrequentedAreaRadiusKm == null) {
        CustomParameters.mostFrequentedAreaRadiusKm = calculateMostFrequentedAreaRadiusKm(waypoints)
    }


    val results = performAnalysis(waypoints)


    if (results != null) {
        writeResultsToJsonFile(results, "evaluation/output.json")
    } else {
        println("Analysis failed.")
    }
}


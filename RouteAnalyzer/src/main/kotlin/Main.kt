package g11

import g11.services.RouteAnalysisService
import g11.utils.validateFiles
import g11.utils.writeResultsToJsonFile


fun main(args: Array<String>) {
    val waypointsCsvPath = args.getOrNull(0) ?: "evaluation/waypoints.csv"
    val yamlFilePath = args.getOrNull(1) ?: "evaluation/custom-parameters.yml"

    if (!validateFiles(waypointsCsvPath, yamlFilePath)) return

    val routeAnalysisService = RouteAnalysisService()
    val results = routeAnalysisService.analyzeRoute(waypointsCsvPath, yamlFilePath)

    if (results != null) {
        writeResultsToJsonFile(results, "evaluation/output.json")
    } else {
        println("Analysis failed.")
    }
}

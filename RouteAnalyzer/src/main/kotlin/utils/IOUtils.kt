package g11.utils

import g11.models.AdvancedAnalysisResults
import java.io.File
import java.io.FileInputStream
import org.yaml.snakeyaml.Yaml
import kotlinx.serialization.json.Json

import g11.models.AnalysisResults
import g11.models.CustomParameters
import g11.models.Waypoint

/**
 * Loads a YAML file and parses it into a map of parameters.
 * This function reads the YAML file located at the specified path and extracts the parameters.
 *
 * @param yamlFilePath Path to the YAML file. The YAML file is expected to contain various keys.
 * @return A map containing the parsed parameters, or null if parsing fails.
 */
fun loadCustomParametersFromYaml(yamlFilePath: String): Map<String, Any>? {
    return try {
        val yaml = Yaml()
        FileInputStream(yamlFilePath).use { inputStream ->
            val data: Map<String, Any> = yaml.load(inputStream) ?: emptyMap()
            data
        }
    } catch (e: Exception) {
        println("Error reading YAML file: ${e.message}")
        null
    }
}


/**
 * Reads waypoints from a CSV file.
 * This function reads a CSV file where each line contains a waypoint's timestamp, latitude, and longitude
 * separated by semicolons. It parses each line into a Waypoint object, skipping any lines that are
 * malformed or contain invalid data.
 *
 * @param filePath Path to the CSV file. The file should contain lines in the format:
 *                 "timestamp;latitude;longitude" (e.g., "1622498100.0;40.7128;-74.0060").
 * @return A list of Waypoint objects parsed from the CSV file. If the file contains invalid or malformed lines,
 *         they will be skipped, and an empty list will be returned in case of failure.
 */
fun readWaypointsFromCsv(filePath: String): List<Waypoint> {
    return try {
        File(filePath).readLines().mapNotNull { line ->
            val parts = line.split(";")
            if (parts.size == 3) {
                try {
                    Waypoint(parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
                } catch (e: NumberFormatException) {
                    println("Skipping invalid line: $line")
                    null
                }
            } else {
                println("Skipping malformed line: $line")
                null
            }
        }
    } catch (e: Exception) {
        println("Error reading CSV file: ${e.message}")
        emptyList()
    }
}

/**
 * Writes the results of an analysis to a JSON file.
 * This function serializes the provided AnalysisResults object into JSON format and writes it to the specified file path.
 *
 * @param results The AnalysisResults object that contains the analysis data to be serialized.
 * @param path The path to the file where the JSON results should be written.
 */
fun writeResultsToJsonFile(results: AnalysisResults, path: String) {
    try {
        val jsonString = Json.encodeToString(AnalysisResults.serializer(), results)
        File(path).writeText(jsonString)
    } catch (e: Exception) {
        println("Error writing to file: ${e.message}")
    }
}

fun writeAdvancedResultsToJsonFile(results: AdvancedAnalysisResults, path: String) {
    try {
        val jsonString = Json.encodeToString(AdvancedAnalysisResults.serializer(), results)
        File(path).writeText(jsonString)
    } catch (e: Exception) {
        println("Error writing to file: ${e.message}")
    }
}

/**
 * Validates the existence of the specified files.
 * This function checks if the waypoints CSV file and the YAML configuration file exist at the given paths.
 *
 * @param waypointsCsvPath Path to the waypoints CSV file.
 * @param yamlFilePath Path to the YAML configuration file.
 * @return `true` if both files exist, `false` otherwise.
 */
fun validateFiles(waypointsCsvPath: String, yamlFilePath: String): Boolean {
    if (!File(waypointsCsvPath).exists()) {
        println("Error: Waypoints CSV file not found at '$waypointsCsvPath'")
        return false
    }
    if (!File(yamlFilePath).exists()) {
        println("Error: YAML configuration file not found at '$yamlFilePath'")
        return false
    }
    return true
}

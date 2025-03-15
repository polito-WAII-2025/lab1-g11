package g11.utils

import java.io.File
import java.io.FileInputStream
import org.yaml.snakeyaml.Yaml
import kotlinx.serialization.json.Json

import g11.models.AnalysisResults
import g11.models.CustomParameters
import g11.models.Waypoint

/**
 * Loads a YAML file and parses it into a CustomParameters object.
 * This function reads the YAML file located at the specified path, extracting the earth radius, geo-fence
 * details, and the most frequented area radius. It also calculates the most frequented area radius from
 * waypoints if it is not provided in the YAML file.
 *
 * @param yamlFilePath Path to the YAML file. The YAML file is expected to contain keys such as "earthRadiusKm",
 *                     "geofenceCenterLatitude", "geofenceCenterLongitude", "geofenceRadiusKm",
 *                     and optionally "mostFrequentedAreaRadiusKm".
 * @param waypoints A list of Waypoint objects that may be used to calculate the most frequented area radius
 *                  if not specified in the YAML file.
 * @return A CustomParameters object containing the parsed and calculated parameters, or null if parsing fails.
 */
fun loadCustomParametersFromYaml(yamlFilePath: String, waypoints: List<Waypoint>): CustomParameters? {
    return try {
        val yaml = Yaml()
        FileInputStream(yamlFilePath).use { inputStream ->
            val data: Map<String, Any> = yaml.load(inputStream)

            val earthRadiusKm = data["earthRadiusKm"] as? Double
                ?: throw IllegalArgumentException("Missing earthRadiusKm in YAML file.")
            val geofenceCenterLatitude = data["geofenceCenterLatitude"] as? Double
                ?: throw IllegalArgumentException("Missing geofenceCenterLatitude in YAML file.")
            val geofenceCenterLongitude = data["geofenceCenterLongitude"] as? Double
                ?: throw IllegalArgumentException("Missing geofenceCenterLongitude in YAML file.")
            val geofenceRadiusKm = data["geofenceRadiusKm"] as? Double
                ?: throw IllegalArgumentException("Missing geofenceRadiusKm in YAML file.")

            val mostFrequentedAreaRadiusKm = data["mostFrequentedAreaRadiusKm"] as? Double
            val finalMostFrequentedAreaRadiusKm =
                mostFrequentedAreaRadiusKm ?: calculateMostFrequentedAreaRadiusKm(waypoints)

            return CustomParameters(
                earthRadiusKm = earthRadiusKm,
                geofenceCenterLatitude = geofenceCenterLatitude,
                geofenceCenterLongitude = geofenceCenterLongitude,
                geofenceRadiusKm = geofenceRadiusKm,
                mostFrequentedAreaRadiusKm = finalMostFrequentedAreaRadiusKm
            )
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
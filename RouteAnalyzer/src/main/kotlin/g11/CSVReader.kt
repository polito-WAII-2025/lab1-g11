package g11

import kotlinx.serialization.Serializable
import java.io.File

// Data class to store waypoints
@Serializable
data class Waypoint(val timestamp: Long, val latitude: Double, val longitude: Double)

// Function to read waypoints from CSV (Handles `;` separator and floating-point timestamps)
fun readWaypoints(filePath: String): List<Waypoint> {
    return File(filePath).readLines().mapNotNull { line ->
        val parts = line.split(";")  // Fix: Use `;` as separator

        if (parts.size < 3) {
            println("Skipping invalid line: $line")
            return@mapNotNull null  // Ignore malformed lines
        }

        try {
            val timestamp = parts[0].trim().toDouble().toLong()  // Fix: Convert float timestamp to Long
            val latitude = parts[1].trim().toDouble()
            val longitude = parts[2].trim().toDouble()
            Waypoint(timestamp, latitude, longitude)
        } catch (e: Exception) {
            println("Error parsing line: $line -> ${e.message}")
            null  // Ignore lines that cannot be parsed
        }
    }
}

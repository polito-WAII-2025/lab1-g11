package g11

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Waypoint(val timestamp: Double, val latitude: Double, val longitude: Double)

fun main() {

    val csv = File("waypoints.csv").readText()

    val data = csv.split("\n").map {
        val parts = it.split(";")
        Waypoint(parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
    }

    data.forEach { println(it) }

}
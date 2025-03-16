package g11.utils

import g11.models.MaxDistanceFromStart
import g11.models.MostFrequentedArea
import g11.models.Waypoint
import g11.models.WaypointsOutsideGeofence
import kotlin.math.*
import g11.models.CustomParameters


/**
 * Converts an angle from degrees to radians.
 *
 * This extension function simplifies the conversion process when working with trigonometric functions.
 *
 * @receiver Angle in degrees.
 * @return The angle in radians.
 */
private fun Double.toRadians(): Double = Math.toRadians(this)

/**
 * Calculates the great-circle distance between two geographical points using the Haversine formula.
 *
 * @param latitude1 Latitude of the first point (in degrees)
 * @param longitude1 Longitude of the first point (in degrees)
 * @param latitude2 Latitude of the second point (in degrees)
 * @param longitude2 Longitude of the second point (in degrees)
 * @param radius Radius of the sphere (default: Earth's radius in km)
 * @return The distance between the two points in kilometers.
 */
private fun distanceInKm(
    latitude1: Double,
    longitude1: Double,
    latitude2: Double,
    longitude2: Double,
    radius: Double = CustomParameters.earthRadiusKm
): Double {
    val dLat = (latitude2 - latitude1).toRadians()
    val dLon = (longitude2 - longitude1).toRadians()

    val a = sin(dLat / 2).pow(2) + cos(latitude1.toRadians()) * cos(latitude2.toRadians()) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return radius * c
}

/**
 * Determines whether a given point is inside a circular geo-fence.
 *
 * @param centerLatitude Latitude of the geo-fence center (in degrees)
 * @param centerLongitude Longitude of the geo-fence center (in degrees)
 * @param radius Radius of the geo-fence (in kilometers)
 * @param latitude Latitude of the point to check (in degrees)
 * @param longitude Longitude of the point to check (in degrees)
 * @return `true` if the point is inside the geo-fence, `false` otherwise.
 */
private fun isPointInGeoFence(
    centerLatitude: Double,
    centerLongitude: Double,
    radius: Double,
    latitude: Double,
    longitude: Double
): Boolean = distanceInKm(centerLatitude, centerLongitude, latitude, longitude) <= radius

/**
 * Calculates the waypoint farthest from the starting waypoint.
 *
 * This function takes a list of waypoints and determines which waypoint is the farthest
 * from the first waypoint in the list. It calculates the distance between the start waypoint
 * and each waypoint using the `distanceInKm` function and returns the farthest waypoint along
 * with the calculated distance.
 *
 * @param waypoints List of waypoints. The first waypoint in the list is considered the starting point.
 * @return MaxDistanceFromStart? An object containing the farthest waypoint and its distance
 *         from the start. Returns null if the list is empty.
 */
fun calculateMaxDistanceFromStart(waypoints: List<Waypoint>): MaxDistanceFromStart? {
    if (waypoints.isEmpty()) return null // Return null if the list is empty

    val start = waypoints.first() // Consider the first waypoint as the starting point
    val maxDistanceWaypoint = waypoints.maxByOrNull {
        // Find the waypoint with the maximum distance from the start
        distanceInKm(start.latitude, start.longitude, it.latitude, it.longitude)
    }

    return maxDistanceWaypoint?.let {
        val maxDistance = distanceInKm(start.latitude, start.longitude, it.latitude, it.longitude)
        MaxDistanceFromStart(it, maxDistance) // Return the farthest waypoint with its distance
    }
}


/**
 * Determines the most frequented area within a given radius.
 *
 * This function iterates through a list of waypoints and identifies the waypoint that has
 * the highest number of nearby waypoints within the specified radius. The function considers
 * each waypoint as a potential center and counts how many other waypoints fall within the radius.
 *
 * @param waypoints List of waypoints to analyze.
 * @param areaRadiusKm Radius (in kilometers) within which waypoints are considered part of the same area.
 * @return MostFrequentedArea? An object containing the central waypoint, the radius, and the count of waypoints within the area.
 */
fun calculateMostFrequentedArea(waypoints: List<Waypoint>, areaRadiusKm: Double): MostFrequentedArea? {
    if (waypoints.isEmpty()) return null // Return null if the list is empty

    var maxCount = 0
    var mostFrequentedWaypoint: Waypoint? = null

    for (waypoint in waypoints) {
        val count = waypoints.count {
            distanceInKm(waypoint.latitude, waypoint.longitude, it.latitude, it.longitude) <= areaRadiusKm
        }

        if (count > maxCount) {
            maxCount = count
            mostFrequentedWaypoint = waypoint
        }
    }

    return mostFrequentedWaypoint?.let {
        MostFrequentedArea(it, areaRadiusKm, maxCount) // Return the most frequented area details
    }
}

/**
 * Counts the waypoints that fall outside a given geo-fence.
 *
 * This function iterates through a list of waypoints and checks whether each waypoint falls within
 * the specified geo-fence. The geo-fence is defined by a central point (latitude and longitude) and a radius.
 * Any waypoint located outside this radius is considered out of bounds.
 *
 * @param waypoints List of waypoints to check.
 * @param geofenceCenterLatitude Latitude of the geo-fence center (in degrees).
 * @param geofenceCenterLongitude Longitude of the geo-fence center (in degrees).
 * @param geofenceRadiusKm Radius (in kilometers) of the geo-fence.
 * @return WaypointsOutsideGeofence? An object containing the geo-fence center, the radius, the count of waypoints
 *         outside the geo-fence, and the list of those waypoints.
 */
fun countWaypointsOutsideGeoFence(
    waypoints: List<Waypoint>,
    geofenceCenterLatitude: Double,
    geofenceCenterLongitude: Double,
    geofenceRadiusKm: Double
): WaypointsOutsideGeofence? {
    if (waypoints.isEmpty()) return null // Return null if the list is empty

    val outsideWaypoints = waypoints.filterNot {
        isPointInGeoFence(geofenceCenterLatitude, geofenceCenterLongitude, geofenceRadiusKm, it.latitude, it.longitude)
    }

    return if (outsideWaypoints.isNotEmpty()) {
        WaypointsOutsideGeofence(
            Waypoint(0.0, geofenceCenterLatitude, geofenceCenterLongitude),
            geofenceRadiusKm,
            outsideWaypoints.size,
            outsideWaypoints
        ) // Return details about waypoints outside the geo-fence
    } else {
        null // Return null if no waypoints are outside the geo-fence
    }
}


/**
 * Calculates a suitable radius for the most frequented area based on the max distance from the start.
 *
 * This function determines a suggested radius by dividing the maximum distance from the starting waypoint
 * by a factor of 10. If the maximum distance is less than 1 km, it defaults to a minimum radius of 0.1 km.
 *
 * @param waypoints List of waypoints to analyze.
 * @return A suggested radius in kilometers. Returns 0.1 km if the waypoints list is empty.
 */
fun calculateMostFrequentedAreaRadiusKm(waypoints: List<Waypoint>): Double {
    val maxDistanceFromStart = calculateMaxDistanceFromStart(waypoints)?.distanceKm ?: return 0.1
    return if (maxDistanceFromStart < 1) 0.1 else maxDistanceFromStart / 10
}

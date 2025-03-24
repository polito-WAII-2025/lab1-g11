package g11.utils

import g11.models.Waypoint
import kotlin.math.*

/**
 * Calculates the total distance traveled given a list of waypoints.
 *
 * @param waypoints A list of Waypoint objects representing the route.
 * @return The total distance traveled in kilometers, or null if the list is empty.
 */
fun calculateTotalDistance(waypoints: List<Waypoint>): Double? {
    if (waypoints.isEmpty()) return null

    var totalDistance: Double = 0.0
    var previousWaypoint: Waypoint = waypoints.first()

    for (waypoint in waypoints) {
        totalDistance += distanceInKm(
            waypoint.latitude,
            waypoint.longitude,
            previousWaypoint.latitude,
            previousWaypoint.longitude
        ).absoluteValue
        previousWaypoint = waypoint
    }

    return totalDistance
}

/**
 * Detects intersection between two geofences.
 *
 * @param latitude1 Latitude of the center of the first geofence.
 * @param longitude1 Longitude of the center of the first geofence.
 * @param radius1 Radius of the first geofence in kilometers.
 * @param latitude2 Latitude of the center of the second geofence.
 * @param longitude2 Longitude of the center of the second geofence.
 * @param radius2 Radius of the second geofence in kilometers.
 * @return Boolean indicating if the two geofences intersect.
 */
fun doGeofencesIntersect(
    latitude1: Double, longitude1: Double, radius1: Double,
    latitude2: Double, longitude2: Double, radius2: Double
): Boolean =
    distanceInKm(latitude1, longitude1, radius1, latitude2, longitude2) <= (radius1 + radius2)


/**
 * Finds the entry and exit points of a geofence.
 *
 * @param waypoints A list of Waypoint objects representing the route.
 *                  The list is ordered by timestamp.
 * @param centerLatitude Latitude of the center of the geofence.
 * @param centerLongitude Longitude of the center of the geofence.
 * @param radius Radius of the geofence in kilometers.
 * @return A Pair of Waypoint objects representing the entry and exit points of the geofence.
 */
fun findGeofenceEntryExitPoints(
    waypoints: List<Waypoint>,
    centerLatitude: Double,
    centerLongitude: Double,
    radius: Double
): Pair<Waypoint?, Waypoint?> {
    var entryPoint: Waypoint? = null
    var exitPoint: Waypoint? = null
    var insideGeofence = false

    for (waypoint in waypoints) {
        val distance = distanceInKm(
            waypoint.latitude,
            waypoint.longitude,
            centerLatitude,
            centerLongitude
        )

        if (distance <= radius) {
            if (!insideGeofence) {
                entryPoint = waypoint
                insideGeofence = true
            }
        } else {
            if (insideGeofence) {
                exitPoint = waypoint
                insideGeofence = false
            }
        }
    }

    return Pair(entryPoint, exitPoint)
}
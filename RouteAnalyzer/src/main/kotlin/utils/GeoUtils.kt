package g11.utils

import g11.models.Waypoint
import kotlin.math.*

// Default Earth radius in kilometers
const val EARTH_RADIUS_KM = 6371.0

/**
 * Converts an angle from degrees to radians.
 *
 * This extension function simplifies the conversion process when working with trigonometric functions.
 *
 * @receiver Angle in degrees.
 * @return The angle in radians.
 */
fun Double.toRadians(): Double = Math.toRadians(this)

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
fun distanceInKm(
    latitude1: Double,
    longitude1: Double,
    latitude2: Double,
    longitude2: Double,
    radius: Double = EARTH_RADIUS_KM
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
fun isPointInGeoFence(
    centerLatitude: Double,
    centerLongitude: Double,
    radius: Double,
    latitude: Double,
    longitude: Double
): Boolean = distanceInKm(centerLatitude, centerLongitude, latitude, longitude) <= radius

/**
 * Calculates the waypoint farthest from the starting waypoint.
 *
 * @param waypoints List of waypoints.
 * @return A pair containing the maximum distance and the corresponding waypoint.
 */
fun calculateMaxDistanceFromStart(waypoints: List<Waypoint>): Pair<Double, Waypoint?> {
    if (waypoints.isEmpty()) return Pair(0.0, null)

    val start = waypoints.first()
    val maxDistanceWaypoint = waypoints.maxByOrNull {
        distanceInKm(start.latitude, start.longitude, it.latitude, it.longitude)
    }

    val maxDistance = maxDistanceWaypoint?.let {
        distanceInKm(start.latitude, start.longitude, it.latitude, it.longitude)
    } ?: 0.0

    return Pair(maxDistance, maxDistanceWaypoint)
}

/**
 * Determines the most frequented area within a given radius.
 *
 * @param waypoints List of waypoints.
 * @param areaRadiusKm Radius to consider for clustering waypoints.
 * @return A pair containing the most frequented waypoint and its count.
 */
fun calculateMostFrequentedArea(waypoints: List<Waypoint>, areaRadiusKm: Double): Pair<Waypoint, Int> {
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

    return Pair(mostFrequentedWaypoint!!, maxCount)
}

/**
 * Counts the waypoints that fall outside a given geo-fence.
 *
 * @param waypoints List of waypoints.
 * @param geofenceCenterLatitude Latitude of the geo-fence center (in degrees).
 * @param geofenceCenterLongitude Longitude of the geo-fence center (in degrees).
 * @param geofenceRadiusKm Radius of the geo-fence (in kilometers).
 * @return A pair containing the count of waypoints outside the geo-fence and the list of those waypoints.
 */
fun countWaypointsOutsideGeoFence(
    waypoints: List<Waypoint>,
    geofenceCenterLatitude: Double,
    geofenceCenterLongitude: Double,
    geofenceRadiusKm: Double
): Pair<Int, List<Waypoint>> {
    val outsideWaypoints = waypoints.filterNot {
        isPointInGeoFence(geofenceCenterLatitude, geofenceCenterLongitude, geofenceRadiusKm, it.latitude, it.longitude)
    }
    return Pair(outsideWaypoints.size, outsideWaypoints)
}


/**
 * Calculates a suitable radius for the most frequented area based on max distance from start.
 *
 * @param waypoints List of waypoints.
 * @return A suggested radius in kilometers.
 */
fun calculateMostFrequentedAreaRadiusKm(waypoints: List<Waypoint>): Double {
    val (maxDistance, _) = calculateMaxDistanceFromStart(waypoints)
    return if (maxDistance < 1) 0.1 else maxDistance / 10
}
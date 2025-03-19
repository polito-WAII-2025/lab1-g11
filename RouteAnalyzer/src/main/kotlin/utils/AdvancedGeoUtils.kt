package g11.utils

import g11.models.Waypoint
import kotlin.math.*


/**
 * README: These are suggested feature that we can implement as extra features.
 * Feel free to add more features if you have something in mind.
 */


/**
 * TODO: Implement a function to detect intersection between two geofence
 * It should receive 6 parameters:
 *  - latitude1: Double
 *  - longitude1: Double
 *  - radius1: Double
 *  - latitude2: Double
 *  - longitude2: Double
 *  - radius2: Double
 *  And return a boolean indicating if the two geofences intersect.
 */


/**
 * TODO: Implement a function to calculate Total Distance Traveled
 * It should receive a list of waypoints and return the total distance traveled in kilometers.
 */

fun calculateTotalDistance (waypoints: List<Waypoint>): Double? {
    if (waypoints.isEmpty()) return null

    var totalDistance: Double = 0.0
    var previousWaypoint: Waypoint = waypoints.first()

    for (waypoint in waypoints) {
        totalDistance += distanceInKm(waypoint.latitude, waypoint.longitude, previousWaypoint.latitude, previousWaypoint.longitude).absoluteValue
        previousWaypoint = waypoint
    }

    return totalDistance
}



/**
 * TODO: Implement a function to find Entry & Exit Points of Geofence
 * It should receive 4 parameters:
 * - waypoints: List<Waypoint>
 *     A list of waypoints representing the route.
 *     The list is ordered by timestamp.
 * - centerLatitude: Double
 * - centerLongitude: Double
 * - radius: Double
 * And return a Pair of Waypoint objects representing the entry and exit points of the geofence.
 */


/**
 * TODO: Implement a function to calculate average speed and maximum speed between waypoints.
 * It should receive a list of waypoints and return a Pair of Doubles representing
 * the average speed and maximum speed.
 */



/**
 * TODO: Implement a function to identify when the user was stationary (speed near zero).
 * It should receive a list of waypoints and return a list of Pair of Doubles representing
 * the start and end timestamps of each stationary period.
 */
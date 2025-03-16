package g11.models

import g11.utils.loadCustomParametersFromYaml

object CustomParameters {
    var earthRadiusKm: Double = 6371.0
    var geofenceCenterLatitude: Double = 0.0
    var geofenceCenterLongitude: Double = 0.0
    var geofenceRadiusKm: Double = 0.0
    var mostFrequentedAreaRadiusKm: Double? = null

    fun load(yamlFilePath: String) {
        val loadedParams = loadCustomParametersFromYaml(yamlFilePath)
        loadedParams?.let {
            earthRadiusKm = it["earthRadiusKm"]?.toString()?.toDoubleOrNull() ?: earthRadiusKm
            mostFrequentedAreaRadiusKm = it["mostFrequentedAreaRadiusKm"]?.toString()?.toDoubleOrNull()
            geofenceCenterLatitude = it["geofenceCenterLatitude"]?.toString()?.toDoubleOrNull() ?: geofenceCenterLatitude
            geofenceCenterLongitude = it["geofenceCenterLongitude"]?.toString()?.toDoubleOrNull() ?: geofenceCenterLongitude
            geofenceRadiusKm = it["geofenceRadiusKm"]?.toString()?.toDoubleOrNull() ?: geofenceRadiusKm
        } ?: throw IllegalArgumentException("Error: Failed to load parameters from '$yamlFilePath'")
    }
}


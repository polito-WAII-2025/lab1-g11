package g11
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.File

// Data class to hold YAML configuration values
data class Config(
    val earthRadiusKm: Double,
    val geofenceCenterLatitude: Double,
    val geofenceCenterLongitude: Double,
    val geofenceRadiusKm: Double,
    val mostFrequentedAreaRadiusKm: Double? = null  // Optional parameter
)

// Function to read YAML configuration
fun readConfig(filePath: String): Config {
    val yamlMapper = YAMLMapper().registerModule(kotlinModule())
    return yamlMapper.readValue(File(filePath), Config::class.java)
}

package g11.models

import kotlinx.serialization.Serializable

@Serializable
data class AdvancedAnalysisResults(
    val averageSpeed: Double?,
    val maxSpeed: Double?,
)

package com.example.in2000_prosjekt.ui

data class LocationInfo( //gi ting navn med også L på slutten
    val temperatureL: Float,
    val fog_area_fractionL : Float,
    val rainL : Float
)
data class NowCastInfo( //sette N eller Now på slutten
    val temperatureNow: Float,
    val windN : Float
)
data class SunriseInfo( //sett S på slutten
    val sunriseS: String,
    val sunsetS: String
)
data class AlertInfo( //sett A på slutten
    val areaA: String,
    val typeA: String,
    val consequenseA: String,
    val recomendationA : String,
    val descriptionA : String,
    val alertTypeA: String,
    val alertLevelA: String
)
data class FrostInfo( //sett Frost på slutten, F brukes allerede
    val typeFrost : String,
    val longFrost : Double,
    val latFrost : Double,
)

sealed interface AppUiState {
    data class Success(
        val locationF: LocationInfo,
        val nowCastF: NowCastInfo,
        val sunriseF: SunriseInfo,
        val alertListF: MutableList<AlertInfo>,
        val frostF: FrostInfo
    ) : AppUiState
    object Error : AppUiState
    object Loading : AppUiState
}
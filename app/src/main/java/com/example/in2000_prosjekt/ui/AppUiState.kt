package com.example.in2000_prosjekt.ui

import com.example.in2000_prosjekt.ui.data.Build
import com.example.in2000_prosjekt.ui.data.Model

data class AppUiState(
    //test med time:
    //val locationForecast: String
    val locationForecast: Model? = null,
    val nowcast: Model? = null,
    val metAlerts : Build? = null
)
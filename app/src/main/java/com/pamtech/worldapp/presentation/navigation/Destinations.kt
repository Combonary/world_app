package com.pamtech.worldapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object ContinentsRoute : NavKey

@Serializable
data class CountriesRoute(val continentCode: String) : NavKey

@Serializable
data class StatesRoute(val countryCode: String) : NavKey

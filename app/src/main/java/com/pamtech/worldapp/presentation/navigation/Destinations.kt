package com.pamtech.worldapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ContinentsRoute

@Serializable
data class CountriesRoute(val continentCode: String)

@Serializable
data class StatesRoute(val countryCode: String)

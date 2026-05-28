package com.pamtech.worldapp.presentation.continents

import com.pamtech.countriesservice.model.Continent

data class ContinentsScreenUIState(
    val isLoading: Boolean = false,
    val continents: List<Continent>? = null,
    val error: String? = null
)

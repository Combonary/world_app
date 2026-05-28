package com.pamtech.worldapp.presentation.countries

import com.pamtech.countriesservice.model.Country

data class CountriesScreenUIState(
    val isLoading: Boolean = false,
    val countries: List<Country>? = null,
    val error: String? = null
)

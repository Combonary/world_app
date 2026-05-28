package com.pamtech.worldapp.presentation.states

import com.pamtech.countriesservice.model.State

data class StatesScreenUIState(
    val isLoading: Boolean = false,
    val states: List<State>? = null,
    val error: String? = null
)
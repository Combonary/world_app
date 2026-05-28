package com.pamtech.worldapp.presentation.states

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamtech.countriesservice.model.LibraryError
import com.pamtech.countriesservice.model.LibraryResult
import com.pamtech.countriesservice.repository.CountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatesViewModel @Inject constructor(
    private val repository: CountriesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<StatesScreenUIState> = MutableStateFlow(StatesScreenUIState())
    val uiState: StateFlow<StatesScreenUIState> = _uiState.asStateFlow()

    private val countryCode: String? = savedStateHandle["countryCode"]

    init {
        countryCode?.let { getStates(it) } ?: run {
            _uiState.value = _uiState.value.copy(error = "Country code is missing")
        }
    }

    private fun getStates(countryCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getStates(countryCode)) {
                is LibraryResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        states = result.data,
                        error = null
                    )
                }
                is LibraryResult.Failure -> {
                    val errorMessage = when (val error = result.error) {
                        is LibraryError.Database -> error.message
                        is LibraryError.Network -> error.message
                        is LibraryError.Unknown -> error.message
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            }
        }
    }
}

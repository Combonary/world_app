package com.pamtech.worldapp.presentation.states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamtech.countriesservice.model.LibraryError
import com.pamtech.countriesservice.model.LibraryResult
import com.pamtech.countriesservice.repository.CountriesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = StatesViewModel.Factory::class)
class StatesViewModel @AssistedInject constructor(
    private val repository: CountriesRepository,
    @Assisted private val countryCode: String?
) : ViewModel() {
    private val _uiState: MutableStateFlow<StatesScreenUIState> = MutableStateFlow(StatesScreenUIState())
    val uiState: StateFlow<StatesScreenUIState> = _uiState.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(countryCode: String?): StatesViewModel
    }

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

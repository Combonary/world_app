package com.pamtech.worldapp.presentation.countries

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

@HiltViewModel(assistedFactory = CountriesViewModel.Factory::class)
class CountriesViewModel @AssistedInject constructor(
    private val repository: CountriesRepository,
    @Assisted private val continentCode: String?
) : ViewModel() {
    private val _uiState: MutableStateFlow<CountriesScreenUIState> = MutableStateFlow(
        CountriesScreenUIState())
    val uiState: StateFlow<CountriesScreenUIState>
        get() = _uiState.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(continentCode: String?): CountriesViewModel
    }

    init {
        continentCode?.let { getCountries(it) } ?: run {
            _uiState.value = _uiState.value.copy(error = "Continent code is missing")
        }
    }

    private fun getCountries(continentCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getCountriesByContinent(continentCode)) {
                is LibraryResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        countries = result.data,
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

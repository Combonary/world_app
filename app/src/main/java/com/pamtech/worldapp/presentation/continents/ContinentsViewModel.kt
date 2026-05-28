package com.pamtech.worldapp.presentation.continents

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
class ContinentsViewModel @Inject constructor(
    private val repository: CountriesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ContinentsScreenUIState> = MutableStateFlow(
        ContinentsScreenUIState())
    val uiState: StateFlow<ContinentsScreenUIState>
        get() = _uiState.asStateFlow()
    
    init {
        getContinents()
    }
    
    private fun getContinents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getContinents()) {
                is LibraryResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        continents = result.data,
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

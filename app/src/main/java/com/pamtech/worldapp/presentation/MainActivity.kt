package com.pamtech.worldapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.pamtech.worldapp.presentation.continents.ContinentsScreen
import com.pamtech.worldapp.presentation.countries.CountriesScreen
import com.pamtech.worldapp.presentation.countries.CountriesViewModel
import com.pamtech.worldapp.presentation.navigation.ContinentsRoute
import com.pamtech.worldapp.presentation.navigation.CountriesRoute
import com.pamtech.worldapp.presentation.navigation.Navigator
import com.pamtech.worldapp.presentation.navigation.StatesRoute
import com.pamtech.worldapp.presentation.navigation.rememberNavigationState
import com.pamtech.worldapp.presentation.navigation.toEntries
import com.pamtech.worldapp.presentation.states.StatesScreen
import com.pamtech.worldapp.presentation.states.StatesViewModel
import com.pamtech.worldapp.presentation.ui.theme.WorldAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorldAppTheme {
                WorldAppApp()
            }
        }
    }
}

@Composable
fun WorldAppApp() {
    val navigationState = rememberNavigationState(
        startRoute = ContinentsRoute,
        topLevelRoutes = setOf(ContinentsRoute)
    )
    val navigator = remember { Navigator(navigationState) }
    val activity = LocalActivity.current

    val entryProvider = entryProvider {
        entry<ContinentsRoute> {
            ContinentsScreen(
                onBackClicked = { activity?.finish() },
                onContinentItemClicked = { continentCode ->
                    navigator.navigate(CountriesRoute(continentCode))
                }
            )
        }
        entry<CountriesRoute> { key ->
            val viewModel = hiltViewModel<CountriesViewModel, CountriesViewModel.Factory>(
                creationCallback = { factory -> factory.create(key.continentCode) }
            )
            CountriesScreen(
                viewModel = viewModel,
                onBackClicked = { navigator.goBack() },
                onCountryItemClicked = { countryCode ->
                    navigator.navigate(StatesRoute(countryCode))
                }
            )
        }
        entry<StatesRoute> { key ->
            val viewModel = hiltViewModel<StatesViewModel, StatesViewModel.Factory>(
                creationCallback = { factory -> factory.create(key.countryCode) }
            )
            StatesScreen(
                viewModel = viewModel,
                onBackClicked = { navigator.goBack() }
            )
        }
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}

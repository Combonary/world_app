package com.pamtech.worldapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pamtech.worldapp.presentation.continents.ContinentsScreen
import com.pamtech.worldapp.presentation.countries.CountriesScreen
import com.pamtech.worldapp.presentation.navigation.ContinentsRoute
import com.pamtech.worldapp.presentation.navigation.CountriesRoute
import com.pamtech.worldapp.presentation.navigation.StatesRoute
import com.pamtech.worldapp.presentation.states.StatesScreen
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
    val navController = rememberNavController()
    val activity = LocalActivity.current

    NavHost(
        navController = navController,
        startDestination = ContinentsRoute
    ) {
        composable<ContinentsRoute> {
            ContinentsScreen(
                onBackClicked = { activity?.finish() },
                onContinentItemClicked = { continentCode ->
                    navController.navigate(CountriesRoute(continentCode))
                }
            )
        }
        composable<CountriesRoute> {
            CountriesScreen(
                onBackClicked = { navController.popBackStack() },
                onCountryItemClicked = { countryCode ->
                    navController.navigate(StatesRoute(countryCode))
                }
            )
        }
        composable<StatesRoute> {
            StatesScreen(
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}

package com.example.projekatrma.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.projekatrma.ui.breeds.BreedsScreen
import com.example.projekatrma.ui.details.DetailsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "breeds"
    ) {
        composable("breeds") {
            BreedsScreen(
                onBreedClick = { breedId ->
                    navController.navigate("details/$breedId")
                }
            )
        }

        composable(
            route = "details/{breedId}",
            arguments = listOf(navArgument("breedId") { type = NavType.StringType })
        ) {
            DetailsScreen()
        }
    }
}

package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.ui.screens.CharacterDetailScreen
import com.example.rickandmorty.ui.screens.CharacterListScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Creates a NavController for navigation.
                    val navController = rememberNavController()
                    // Defines the navigation graph.
                    NavHost(navController = navController, startDestination = "characterList") {
                        // Defines the route for the character list screen.
                        composable("characterList") {
                            // Displays the character list screen.
                            CharacterListScreen(onCharacterClick = { characterId ->
                                // Navigates to the character detail screen when an item is clicked.
                                navController.navigate("characterDetail/$characterId")
                            })
                        }
                        // Defines the route for the character detail screen.
                        composable(
                            "characterDetail/{characterId}",
                            // Arguments for the character detail screen.
                            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            // Retrieves the character ID from the navigation arguments.
                            val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
                            // Displays the character detail screen.
                            CharacterDetailScreen(
                                characterId = characterId,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


package com.example.quizly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.NavType
import androidx.navigation.navArgument

import com.google.firebase.FirebaseApp

import com.example.quizly.Database.CardViewModel
import com.example.quizly.Database.AppDatabase
import com.example.quizly.Database.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 1️⃣  make sure Firebase is ready */
        FirebaseApp.initializeApp(this)          // safe to call even if already init’d

        /* 3️⃣  everything else in your setContent {} stays the same */
        try {
            setContent {
                MaterialTheme(colorScheme = lightColorScheme(primary = Color.Black)) {
                    Log.d("MainActivity", "✅ Beginning App.")

                    val navController = rememberNavController()

                    val database = AppDatabase.getInstance(applicationContext)
                    val cardDao   = database.cardDao()
                    val viewModel = ViewModelProvider(
                        this@MainActivity,
                        ViewModelFactory(cardDao)
                    )[CardViewModel::class.java]

                    Log.d("MainActivity", "✅ Database created.")

                    NavHost(navController, startDestination = "auth") {
                        composable("auth")     { AuthScreen(navController = navController) }
                        composable("home")     { MyApp(navController, viewModel) }
                        composable("addCard") {
                            AddCard(
                                viewModel = viewModel,
                                onBack = { navController.navigateUp() }  // This handles the back navigation
                            )
                        }
                        composable("addCard") {
                            AddCard(
                                viewModel = viewModel,
                                onBack = { navController.navigateUp() }  // This handles the back navigation
                            )
                        }
                        composable(
                            route = "editCard/{cardId}",                       // ← placeholder
                            arguments = listOf(
                                navArgument("cardId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            // 1️⃣  throw (or early-return) if arguments are missing
                            val id = backStackEntry.arguments?.getInt("cardId")
                                ?: return@composable              // or: error("cardId missing")
                            EditCard(
                                viewModel = viewModel,
                                cardId = id,
                                onBack = { navController.navigateUp() }
                            )       // pass it in
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ Error: ${e.message}", e)
            setContent { Text("App crashed: ${e.message}") }
        }
    }
}

















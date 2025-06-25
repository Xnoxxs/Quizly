
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

import com.example.quizly.Database.CardViewModel
import com.example.quizly.Database.AppDatabase
import com.example.quizly.Database.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContent {
                MaterialTheme(
                    colorScheme = lightColorScheme(primary = Color.Black),
                ) {
                    Log.d("MainActivity", "✅ Beginning App.")

                    val navController = rememberNavController()

                    val database = AppDatabase.getInstance(applicationContext)
                    val cardDao = database.cardDao()
                    val viewModel = ViewModelProvider(this, ViewModelFactory(cardDao))[CardViewModel::class.java]

                    Log.d("MainActivity", "✅ Database created.")
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            MyApp(navController=navController, viewModel=viewModel)
                        }
                        composable("addCard") {
                            AddCard(viewModel=viewModel)
                        }
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e("MainActivity", "❌ Error: ${e.message}", e)
            e.printStackTrace()
            setContent {
                Text("App crashed: ${e.message}")
            }
        }
    }
}


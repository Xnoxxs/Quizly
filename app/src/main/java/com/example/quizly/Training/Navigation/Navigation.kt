package com.example.quizly.Training.Navigation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController

class TrainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⇢ Firebase init (equivalent to Firebase.initializeApp in Flutter)
        //FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(primary = Color.Black),
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "training"
                ) {
                    composable("training") {
                        TrainingScreen(
                            navController = navController,
                        )
                    }
                    composable("other") {
                        OtherScreen()
                    }
                }
            }
        }
    }
}



@Composable
private fun TrainingScreen(navController: NavHostController) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {

            Button(
                onClick = {
                    navController.navigate("other")
                },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text("Go")
            }
        }
    }
}

@Composable
private fun OtherScreen() {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "OtheScreen",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}


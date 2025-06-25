package com.example.quizly.Training.StateManagement


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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData

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
                val viewModel: SharedViewModel = viewModel()  // Created only once for the whole activity

                NavHost(
                    navController = navController,
                    startDestination = "training"
                ) {
                    composable("training") {
                        TrainingScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable("other") {
                        OtherScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}



class SharedViewModel : ViewModel() {
    private val _selectedWord = MutableLiveData<String?>(null)
    val selectedWord: LiveData<String?> = _selectedWord

    fun setSelectedWord(word: String) {
        _selectedWord.value = word
    }
}


@Composable
private fun TrainingScreen(navController: NavHostController, viewModel: SharedViewModel) {

    val selectedWord by viewModel.selectedWord.observeAsState()

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
                text = "Training Screen",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = selectedWord ?: "No word selected",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.setSelectedWord("ok")
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
private fun OtherScreen(viewModel: SharedViewModel) {

    val selectedWord by viewModel.selectedWord.observeAsState()

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
                text = selectedWord ?: "No word selected",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}


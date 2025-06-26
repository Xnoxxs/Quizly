package com.example.quizly.Training.SnackBar


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { TopSnackbarScreen() } }
    }
}

@Composable
fun TopSnackbarScreen() {
    /* 1) normal snackbar machinery */
    val hostState = remember { SnackbarHostState() }
    val scope     = rememberCoroutineScope()

    /* 2) no Scaffold this time — we control placement fully */
    Box(Modifier.fillMaxSize()) {

        /* ----------  MAIN CONTENT (centre) ---------- */
        Button(
            onClick = {
                scope.launch {
                    hostState.showSnackbar(
                        message = "Snack up here!",
                        actionLabel = "Hide",
                        withDismissAction = true
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Text("Show TOP snackbar")
        }

        /* ----------  SNACKBAR HOST (top-center) ---------- */
        SnackbarHost(
            hostState = hostState,
            modifier   = Modifier
                .align(Alignment.TopCenter)    // ← right here
                .padding(top = 16.dp)
        )
    }
}

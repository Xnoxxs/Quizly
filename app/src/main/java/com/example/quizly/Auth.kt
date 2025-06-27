package com.example.quizly

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavHostController) {
    val auth          = remember { FirebaseAuth.getInstance() }
    val snackbarHost  = remember { SnackbarHostState() }
    val scope         = rememberCoroutineScope()

    var email        by remember { mutableStateOf("hamza.jbk.17@gmail.com") }
    var password     by remember { mutableStateOf("okokok") }
    var pwVisible    by remember { mutableStateOf(false) }
    var currentUid   by remember { mutableStateOf<String?>(null) }

    /* dialog state */
    var showDialog    by remember { mutableStateOf(false) }
    var dialogTitle   by remember { mutableStateOf("") }
    var dialogMsg     by remember { mutableStateOf("") }

    /* update UID whenever auth state changes */
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { fb ->
            currentUid = fb.currentUser?.uid
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        //topBar = { CenterAlignedTopAppBar { Text("Firebase Log-in") } }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(24.dp)
        ) {
            /*  email  */
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /*  password  */
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation =
                    if (pwVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { pwVisible = !pwVisible }) {
                        Icon(
                            if (pwVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            /*  LOG-IN button  */
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        dialogTitle = "Missing data"
                        dialogMsg   = "Please enter both e-mail and password."
                        showDialog  = true
                        return@Button
                    }

                    scope.launch {
                        try {
                            val uid = loginUser(email, password)
                            navController.navigate("home")

                        } catch (e: Exception) {

                            Log.e("Errorrrr", e.localizedMessage ?: "Unknown error")

                            dialogTitle = "Log-in failed"
                            dialogMsg   = e.localizedMessage ?: "Unknown error"
                            showDialog  = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Log in") }

            /*  show UID */
            /*
            currentUid?.let {
                Spacer(Modifier.height(32.dp))
                Text("Current user UID:\n$it", style = MaterialTheme.typography.bodyMedium)
            }
             */
        }
    }

    /* ── pop-up dialog ─────────────────────────────── */
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("OK") }
            },
            title  = { Text(dialogTitle) },
            text   = { Text(dialogMsg) },
            shape  = RoundedCornerShape(16.dp)
        )
    }
}



suspend fun loginUser(email: String, password: String): String {
    val auth = FirebaseAuth.getInstance()

    auth.currentUser?.uid?.let { return it }     // already signed-in

    val result = auth.signInWithEmailAndPassword(email, password).await()
    return result.user?.uid
        ?: throw IllegalStateException("Sign-in succeeded but user == null")
}

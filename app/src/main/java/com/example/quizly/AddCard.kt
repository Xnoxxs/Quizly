package com.example.quizly

import android.util.Log
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import com.example.quizly.Database.CardViewModel

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Scaffold


import kotlinx.coroutines.launch


@Composable
fun AddCard(
    viewModel: CardViewModel,
    onBack: () -> Unit = {}
) {

    var card        by remember { mutableStateOf("") }
    var value       by remember { mutableStateOf("") }

    /* ---------- snackbar host ---------- */
    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            Box(Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier  = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 12.dp)          // small inset from status bar
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            /* ---------- Top bar ---------- */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(Modifier.weight(1f))
                Text("Add", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(48.dp))
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = card,
                    onValueChange = { card = it },
                    label = { Text("Add Card") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Add Value") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.weight(1f))

            /* ---------- Add button ---------- */
            Button(
                onClick = {
                    scope.launch {
                        try {
                            viewModel.addCard(card, value)
                            snackbarHostState.showSnackbar(
                                message  = "Card added!",
                                duration = SnackbarDuration.Short
                            )
                            // clear fields after success
                            card  = ""
                            value = ""
                        } catch (e: Exception) {
                            Log.e("AddCard", "Failed to add card", e)
                            snackbarHostState.showSnackbar(
                                message  = "Failed to add card",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape  = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Add", color = Color.White)
            }
        }
    }
}



@Composable
fun ExtraRow(
    keyText: String, // hello
    valueText: String, // salut
    extras: MutableState<Map<String, String>>, // {"hello": "salut", "time": "temp"}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Text - bold
        Text(
            text = keyText,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )

        // Center Text - bold, teal
        Text(
            text = valueText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B) // or MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f),
        )

        // Delete Icon Button - circular black with white icon
        IconButton(
            onClick = {
                // Remove {"hello": "salut"}
                // from {"hello": "salut", "time": "temp"}
                extras.value = extras.value - keyText

            },
            modifier = Modifier
                .size(36.dp)
                .background(Color.Black, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}



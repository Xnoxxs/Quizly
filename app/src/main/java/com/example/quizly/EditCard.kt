
package com.example.quizly

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizly.Database.CardViewModel
import kotlinx.coroutines.launch


@Composable
fun EditCard(
    viewModel: CardViewModel,
    cardId: Int,
    onBack: () -> Unit = {}
) {
    /* ---------- Snackbar host ---------- */
    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    /* ---------- text-field ---------- */
    var cardText  by remember { mutableStateOf("") }
    var cardValue by remember { mutableStateOf("") }

    /* ---------- Load card once ---------- */
    LaunchedEffect(cardId) {
        viewModel.getCardById(cardId) { row ->
            row?.let {
                cardText  = it.card
                cardValue = it.value
            }
        }
    }

    Scaffold(
        snackbarHost = {
            Box(Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier  = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 12.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                Text("Edit", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(48.dp))
            }

            /* ---------- Text fields ---------- */
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = cardText,
                    onValueChange = { cardText = it },
                    label = { Text("Card") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cardValue,
                    onValueChange = { cardValue = it },
                    label = { Text("Value") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.weight(1f))

            /* ---------- Save button ---------- */
            Button(
                onClick = {
                    scope.launch {
                        viewModel.updateCard(cardId, cardText, cardValue)
                        snackbarHostState.showSnackbar(
                            message = "Card updated successfully",
                            duration = SnackbarDuration.Short
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape  = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}

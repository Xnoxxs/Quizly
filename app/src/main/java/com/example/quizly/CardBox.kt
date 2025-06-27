package com.example.quizly

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

import com.example.quizly.Database.Card
import com.example.quizly.Database.CardViewModel


@Composable
fun CardBox(
    currentCard: Card?,
    navController: NavHostController,
    viewModel: CardViewModel,
    onDelete: () -> Unit
) {
    var isFlipped  by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val rotationYValue by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500),
        label = "flipAnimation"
    )

    /* ---------- DIALOG ---------- */
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title   = { Text("Delete card?") },
            text    = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    currentCard?.let { viewModel.deleteCard(it.id) }
                    onDelete()
                    showDialog = false
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }

    /* ---------- CARD UI ---------- */
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { isFlipped = !isFlipped }
                .graphicsLayer {
                    rotationY = rotationYValue
                    cameraDistance = 8 * density
                }
                .background(Color.LightGray)
        ) {
            if (rotationYValue <= 90f) {
                /* front */
                Text(
                    text  = currentCard?.card ?: "No word selected",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )

                if(currentCard != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        verticalAlignment  = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                       /*
                        Button(
                            onClick = { /* TODO add-to-test */ },
                            shape   = CircleShape,
                            colors  = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) { Text("Add To Test", color = Color.White) }
                        */

                        Row {

                            IconButton(
                                onClick = { currentCard.let { navController.navigate("editCard/${it.id}") } }
                            ) { Icon(Icons.Default.Edit, contentDescription = "Edit") }

                            IconButton(
                                onClick = { showDialog = true }
                            ) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
                        }
                    }
                }
            } else {
                /* back */
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f },
                    contentAlignment = Alignment.Center
                ) {
                    Text(currentCard?.value ?: "", style = MaterialTheme.typography.headlineMedium)
                }
            }

        }
        Spacer(Modifier.height(16.dp))
        if(currentCard != null && isFlipped) {
            ExampleScreen(currentCard.value)

        }
    }
}


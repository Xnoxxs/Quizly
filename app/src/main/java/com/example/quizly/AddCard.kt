

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


@Composable
fun AddCard(
    viewModel: CardViewModel,
) {

    val cards by viewModel.cards.collectAsState()

    var card by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    var isAddExtra by remember { mutableStateOf(false) }
    var extra by remember { mutableStateOf("") }
    var extraValue by remember { mutableStateOf("") }

    var extras = remember { mutableStateOf(mapOf<String, String>()) }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("Add", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(48.dp)) // space to balance arrow
        }

        LazyColumn {
            items(cards) { card ->
                LaunchedEffect(card.id) {
                    Log.d("CardScreen", "Rendering card: ${card.value}")
                }
                Text(card.value, modifier = Modifier.padding(vertical = 4.dp))
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        // Extra + Plus Button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Extra", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { isAddExtra = true },
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.Black, shape = CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Extra", tint = Color.White)
            }
        }

        if(isAddExtra == true) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = extra,
                        onValueChange = { extra = it},
                        label = { Text("Add Extra") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = extraValue,
                        onValueChange = { extraValue = it},
                        label = { Text("Add Value") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(
                    onClick = {
                        println("✅ App started")

                        if (extra.isNotBlank() && extraValue.isNotBlank()) {
                            // Add to Extras
                            println("✅ Great")
                            // Add a key-value pair
                            extras.value = extras.value + (extra to extraValue)
                            Log.d("extras", extras.toString())
                        } else {
                            Log.d("extras", "Nothing ")

                        }
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Add", color = Color.White)
                }
            }
        }

        extras.value.forEach { (key, value) ->
            ExtraRow(keyText = key, valueText = value, extras = extras)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add Button (Bottom)
        Button(
            onClick = {

                // card = "hello"
                // value = "salut"
                // extras = {"time": "temp"}
                Log.d("AddCard", "Logging")
                try {
                    viewModel.addCard(card, value)
                } catch (e: Exception) {
                    Log.e("AddCard", "Failed to add card: ${e.message}", e)
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Add", color = Color.White)
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



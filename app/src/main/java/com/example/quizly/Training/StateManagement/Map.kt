package com.example.quizly.Training.StateManagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MapScreen()
            }
        }
    }
}

@Composable
fun MapScreen() {
    val mapState = remember { mutableStateOf(mapOf("Apple" to "Red", "Banana" to "Yellow")) }
    var newKey by remember { mutableStateOf("") }
    var newValue by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Fruit Colors", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newKey,
            onValueChange = { newKey = it },
            label = { Text("Key") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newValue,
            onValueChange = { newValue = it },
            label = { Text("Value") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ElevatedButton(
            onClick = {
                if (newKey.isNotBlank() && newValue.isNotBlank()) {
                    mapState.value = mapState.value + (newKey to newValue)
                    newKey = ""
                    newValue = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Map")
        }

        Spacer(modifier = Modifier.height(16.dp))

        mapState.value.forEach { (key, value) ->
            MapRow(keyText = key, valueText = value, mapState = mapState)
        }
    }
}

@Composable
fun MapRow(
    keyText: String,
    valueText: String,
    mapState: MutableState<Map<String, String>>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valueText,
            color = Color(0xFF00796B),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                // Remove this key from the map
                mapState.value = mapState.value - keyText
            },
            modifier = Modifier
                .size(36.dp)
                .background(Color.Black, shape = CircleShape)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.White)
        }
    }
}

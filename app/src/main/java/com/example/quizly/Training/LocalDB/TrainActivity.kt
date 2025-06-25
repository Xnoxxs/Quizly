
package com.example.quizly.Training.LocalDB

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.quizly.Training.LocalDB.AppDatabase
import com.example.quizly.Training.LocalDB.ViewModelFactory
import com.example.quizly.Training.LocalDB.CardViewModel

class TrainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            println("⏳ Initializing database...")
            val database = AppDatabase.getInstance(applicationContext)
            println("✅ Database created.")

            val wordDao = database.cardDao()
            println("✅ DAO acquired.")

            val viewModel = ViewModelProvider(this, ViewModelFactory(wordDao))[CardViewModel::class.java]
            println("✅ ViewModel created.")

            setContent {
                println("🖼️ Setting content...")
                MaterialTheme {
                    CardScreen(viewModel)
                }
            }

        } catch (e: Exception) {
            Log.e("TrainActivity", "❌ Error: ${e.message}", e)
            println("❌ Exception caught: ${e.message}")
            e.printStackTrace()
            setContent {
                Text("App crashed: ${e.message}")
            }
        }
    }
}

@Composable
fun CardScreen(viewModel: CardViewModel) {
    val cards by viewModel.cards.collectAsState()
    var input by remember { mutableStateOf("") }
    var cardId by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter a card value") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.addCard(input, "ok")
                input = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Card")
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(cards) { card ->
                LaunchedEffect(card.id) {
                    Log.d("CardScreen", "Rendering card: ${card.value}")
                }
                Text(card.value, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = cardId,
            onValueChange = { cardId = it },
            label = { Text("Enter card ID to update") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.updateCardValue(cardId.toInt(), "AAAA")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Card $cardId to 'AAAA'")
        }
    }
}

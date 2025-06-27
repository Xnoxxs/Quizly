package com.example.quizly

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizly.Database.Card
import kotlin.collections.forEach

@Composable
fun Search(
    currentCard : (Card?) -> Unit,
    cards       : List<Card>,
    onClose     : () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    /* -------- reacts to every keystroke ---------- */
    val filteredCards = remember(searchText, cards) {
        if (searchText.isBlank()) cards
        else cards.filter { it.card.contains(searchText, ignoreCase = true) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {

            /* ------------- top bar (unchanged) ------------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onClose) { Icon(Icons.Default.ArrowBack, null) }
                Text("Search", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = onClose) { Icon(Icons.Default.Close,  null) }
            }

            Spacer(Modifier.height(16.dp))

            /* ------------------ search box ----------------- */
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            /* --------------- results list ------------------ */
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                if (filteredCards.isEmpty()) {
                    Text(
                        "No matches",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    filteredCards.forEach { card ->
                        Text(
                            text = card.card,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentCard(card)
                                    onClose()
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

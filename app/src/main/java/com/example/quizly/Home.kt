package com.example.quizly

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import kotlin.random.Random




import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizly.Database.Card
import com.example.quizly.Database.CardViewModel

import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text


fun getRandomCard(cards: List<Card>): Card? {
    return if (cards.isNotEmpty()) {
        cards[Random.nextInt(cards.size)]
    } else {
        null
    }
}

@Composable
fun MyApp(navController: NavHostController, viewModel: CardViewModel) {

    val cards by viewModel.cards.collectAsState()

    Log.d("MyApp", "cards: $cards")

    /* ---------- State (replaces Riverpod providers) ---------- */
    var currentMode by remember { mutableStateOf("Words") }
    var amountOfWords by remember { mutableStateOf(0) }

    var currentCard by remember { mutableStateOf<Card?>(null) }

    val categories = listOf("Words", "Test")

    var isSearchBarVisible by remember { mutableStateOf(false) }

    /* --------------------------- UI -------------------------- */
    Scaffold(
        //topBar = { TopAppBar(modifier = Modifier.height(0.dp)) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 60.dp),
                onClick = {
                    amountOfWords += 1
                    currentCard = getRandomCard(cards)

                },
                containerColor = Color.Black
            ) { Icon(Icons.Default.Shuffle, null, tint = Color.White) }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 15.dp)
            )
        }
    ) {
            paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            if (isSearchBarVisible) {
                Search(
                    currentCard = { card -> currentCard = card },
                    cards = cards,
                    onClose = { isSearchBarVisible = false }
                )
            }

            Spacer(Modifier.height(30.dp))


            /* ---------------- Search Button ---------------- */
            Button(
                {
                    isSearchBarVisible = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.fillMaxWidth()       // ← makes it span the full width
            ) {
                Text("Search", color = Color.White)
            }

            Spacer(Modifier.height(30.dp))


            /* ---------------- Header Row ------------------ */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BigText(currentMode)
                SmallText(" (${cards.size})")

                Spacer(Modifier.width(20.dp))
                MyIconButton(icon = Icons.Default.ChangeCircle) {
                    currentCard = null
                    val next = (categories.indexOf(currentMode) + 1) % categories.size
                    currentMode = categories[next]
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ------------- Counter + Add button ------------ */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SmallText("$amountOfWords / ${cards.size}")
                    Spacer(Modifier.width(40.dp))
                    Icon(
                        Icons.Default.ReplayCircleFilled, null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { amountOfWords = 0 }
                    )
                }
                MyIconButton(icon = Icons.Default.Add) {
                    navController.navigate("addCard")

                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- Main Content ---------------- */
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .border(1.dp, Color.Black)
                    .padding(vertical = 10.dp)
            ) {
                CardBox(
                    currentCard = currentCard,
                    navController = navController,
                    viewModel = viewModel
                ) {
                    currentCard = null            // parent owns the state, so it can mutate it
                    amountOfWords -= 1
                }
            }

        }
    }
}



@Composable
fun MyIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    IconButton(onClick) { Icon(icon, null, tint = Color.Black) }
}


@Composable
fun BigText(text: String) =
    Text(text, style = MaterialTheme.typography.headlineMedium)

@Composable
fun SmallText(text: String) =
    Text(text, style = MaterialTheme.typography.bodyLarge)


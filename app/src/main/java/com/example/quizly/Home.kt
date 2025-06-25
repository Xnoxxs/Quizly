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

    // Firebase stream placeholder; will hook Firestore Flow later
    val words by remember { mutableStateOf<Map<String, Map<String, Any>>>(emptyMap()) }

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

            /* ---------------- Search Button ---------------- */
            Button(
                {
                    isSearchBarVisible = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                Text("Search", color = Color.White)
            }

            Spacer(Modifier.height(30.dp))

            if (isSearchBarVisible) {
                FullScreenSearchBar(
                    currentCard = { card -> currentCard = card },
                    cards = cards,
                    onClose = { isSearchBarVisible = false }
                )
            }

            /* ---------------- Header Row ------------------ */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BigText(currentMode)
                Text(" (${cards.size})", style = MaterialTheme.typography.bodySmall)

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
                    SmallText("$amountOfWords / ${words.size}")
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
                    card = currentCard
                )
                Divider(thickness = 2.dp, color = Color.Black)
                Spacer(Modifier.height(20.dp))
                Examples(words)
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
    Text(text, style = MaterialTheme.typography.bodySmall)


@Composable
fun CardBox(
    card: Card?, // Card(id=1, card="oooo", value="aaaa")
    onAddToTest: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotationYValue by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "flipAnimation"
    )

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
        // Front side
        if (rotationYValue <= 90f) {
            Text(
                text = card?.card ?: "No word selected",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onAddToTest,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Add To Test", color = Color.White)
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        } else {
            // Back side
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = card?.value ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}



@Composable
fun Examples(words: Map<String, Map<String, Any>>) {
    /* TODO: real examples list */
    Text("Examples placeholder")
}

@Composable
fun FullScreenSearchBar(
    currentCard: (Card?) -> Unit,
    cards: List<Card>,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text("Search", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            Spacer(Modifier.height(16.dp))
            var searchText by remember { mutableStateOf("") }
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            // Add your list of search results here
            // For example:
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                cards.forEach { card ->
                    Text(
                        text = card.card,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                currentCard(card)
                                Log.d("SelectedCard", "You selected: ${card.card}")
                                onClose()
                            }
                    )
                }
            }

        }
    }
}


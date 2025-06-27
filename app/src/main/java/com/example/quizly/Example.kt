package com.example.quizly

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.quizly.Unsplash.UnsplashPhoto
import com.example.quizly.Unsplash.UnsplashProvider

@Composable
fun ExampleScreen(item: String) {

    val photoState = produceState<UnsplashPhoto?>(initialValue = null) {
        value = UnsplashProvider.fetchPhoto(item)
    }

    val photo = photoState.value

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when {
            photo == null -> {
                Text("Loading image...")
            }
            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    /*
                    Text("Uploaded by: ${photo.user.name}")
                    Spacer(modifier = Modifier.height(16.dp))
                     */
                    AsyncImage(
                        model = photo.urls.small,
                        contentDescription = photo.user.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }
        }

    }
}


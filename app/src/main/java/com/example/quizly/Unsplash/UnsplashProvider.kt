
package com.example.quizly.Unsplash

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val UNSPLASH_KEY = "FGlqDzxMdPd9XHfmNengAPycuk5WakaqVPzXus1i3Rc"

object UnsplashProvider {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(UnsplashApiService::class.java)

    suspend fun fetchPhoto(query: String): UnsplashPhoto? = withContext(Dispatchers.IO) {
        try {
            val response = api.searchPhotos(query, UNSPLASH_KEY)
            Log.d("UNSPLASH", "HTTP ${'$'}{response.code()}  body=${'$'}{response.body()}")
            if (response.isSuccessful) {
                response.body()?.results?.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UNSPLASH", "Exception: $e")
            null
        }
    }
}
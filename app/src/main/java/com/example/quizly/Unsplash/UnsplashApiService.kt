
package com.example.quizly.Unsplash

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


data class UnsplashResponse(val results: List<UnsplashPhoto>)
data class UnsplashPhoto(val id: String, val urls: UnsplashPhotoUrls, val user: UnsplashUser)
data class UnsplashPhotoUrls(val small: String, val regular: String)
data class UnsplashUser(val name: String)

// API interface -----------------------------------------------------
interface UnsplashApiService {
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query")     query: String,
        @Query("client_id") clientId: String,
        @Query("page")      page: Int = 1,
        @Query("per_page")  perPage: Int = 10,
    ): Response<UnsplashResponse>
}

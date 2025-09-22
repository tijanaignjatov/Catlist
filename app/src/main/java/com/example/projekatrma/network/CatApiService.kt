package com.example.projekatrma.network

import com.example.projekatrma.model.Breed
import com.example.projekatrma.model.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @GET("breeds")
    suspend fun getAllBreeds(): List<Breed>

    @GET("images/search")
    suspend fun getImageForBreed(
        @Query("breed_ids") breedId: String
    ): List<ImageResponse>
}



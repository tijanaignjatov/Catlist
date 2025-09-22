package com.example.projekatrma.repository

import com.example.projekatrma.model.Breed
import com.example.projekatrma.model.ImageResponse
import com.example.projekatrma.network.CatApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatRepository @Inject constructor(
    private val api: CatApiService
) {
    suspend fun getAllBreeds(): List<Breed> = withContext(Dispatchers.IO) {
        api.getAllBreeds()
    }

    suspend fun getImageForBreed(breedId: String): List<ImageResponse> = withContext(Dispatchers.IO) {
        api.getImageForBreed(breedId)
    }
}


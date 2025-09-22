package com.example.projekatrma.ui.details

import com.example.projekatrma.model.Breed

interface BreedDetailsContract {

    data class UiState(
        val breedId: String,
        val isLoading: Boolean = true,
        val breed: Breed? = null,
        val imageUrl: String? = null,
        val errorMessage: String? = null,
    )

    sealed class UiEvent {
        data object LoadBreed : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
    }
}

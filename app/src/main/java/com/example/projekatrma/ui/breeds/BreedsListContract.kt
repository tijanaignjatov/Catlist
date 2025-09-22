package com.example.projekatrma.ui.breeds

import com.example.projekatrma.model.Breed

interface BreedsListContract {

    data class UiState(
        val isLoading: Boolean = false,
        val breeds: List<Breed> = emptyList(),
        val errorMessage: String? = null
    )

    sealed class UiEvent {
        data object LoadBreeds : UiEvent()
        data class Search(val query: String) : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
    }
}

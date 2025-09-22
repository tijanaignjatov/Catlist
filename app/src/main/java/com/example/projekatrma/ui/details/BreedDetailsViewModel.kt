package com.example.projekatrma.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekatrma.repository.CatRepository
import com.example.projekatrma.ui.details.BreedDetailsContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CatRepository
) : ViewModel() {

    private val breedId: String = checkNotNull(savedStateHandle["breedId"])

    private val _state = MutableStateFlow(UiState(breedId = breedId))
    val state = _state.asStateFlow()

    private val events = MutableSharedFlow<UiEvent>()
    fun setEvent(event: UiEvent) = viewModelScope.launch { events.emit(event) }

    private val _effect = Channel<SideEffect>()
    val effect = _effect.receiveAsFlow()
    private fun setEffect(effect: SideEffect) = viewModelScope.launch { _effect.send(effect) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    UiEvent.LoadBreed -> loadBreedDetails()
                }
            }
        }
    }

    private fun loadBreedDetails() = viewModelScope.launch {
        try {
            val all = repository.getAllBreeds()
            val selected = all.firstOrNull { it.id == breedId }

            val imageUrl = repository.getImageForBreed(breedId).firstOrNull()?.url

            _state.update {
                it.copy(
                    breed = selected,
                    imageUrl = imageUrl,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(isLoading = false, errorMessage = "Greška: ${e.message}")
            }
            setEffect(SideEffect.ShowToast("Greška: ${e.message}"))
        }
    }
}

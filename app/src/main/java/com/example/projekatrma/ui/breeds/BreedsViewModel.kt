package com.example.projekatrma.ui.breeds

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekatrma.model.Breed
import com.example.projekatrma.repository.CatRepository
import com.example.projekatrma.ui.breeds.BreedsListContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val repository: CatRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var fullList: List<Breed> = emptyList()

    private val _state = MutableStateFlow(BreedsListContract.UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsListContract.UiState.() -> BreedsListContract.UiState) =
        _state.update(reducer)

    private val events = MutableSharedFlow<BreedsListContract.UiEvent>()
    fun setEvent(event: BreedsListContract.UiEvent) = viewModelScope.launch { events.emit(event) }

    private val _effect = Channel<SideEffect>()
    val effect = _effect.receiveAsFlow()
    private fun setEffect(effect: BreedsListContract.SideEffect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private var currentQuery: String
        get() = savedStateHandle["query"] ?: ""
        set(value) {
            savedStateHandle["query"] = value
        }

    fun getSavedQuery(): String = currentQuery

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is BreedsListContract.UiEvent.LoadBreeds -> loadBreeds()
                    is BreedsListContract.UiEvent.Search -> {
                        currentQuery = event.query
                        filterBreeds(currentQuery)
                    }
                }
            }
        }
    }

    private fun loadBreeds() = viewModelScope.launch {
        setState { copy(isLoading = true, errorMessage = null) }
        try {
            val breeds = repository.getAllBreeds()
            fullList = breeds
            filterBreeds(currentQuery)
        } catch (e: Exception) {
            setState {
                copy(
                    isLoading = false,
                    errorMessage = "Greška: ${e.message}"
                )
            }
        }
    }

    private fun filterBreeds(query: String) {
        val filtered = if (query.isBlank()) fullList
        else fullList.filter { it.name.contains(query, ignoreCase = true) }

        setState { copy(breeds = filtered, isLoading = false) }
    }
}


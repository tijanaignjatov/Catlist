package com.example.projekatrma.ui.breeds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.projekatrma.model.Breed
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.projekatrma.ui.breeds.BreedsViewModel
import com.example.projekatrma.ui.breeds.BreedsListContract
import com.example.projekatrma.ui.breeds.BreedsListContract.UiEvent
import com.example.projekatrma.ui.breeds.BreedsListContract.UiState


@Composable
fun BreedsScreen(
    onBreedClick: (String) -> Unit,
    viewModel: BreedsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(viewModel.getSavedQuery()))
    }


    LaunchedEffect(Unit) {
        viewModel.setEvent(BreedsListContract.UiEvent.LoadBreeds)
        if (query.text.isNotBlank()) {
            viewModel.setEvent(BreedsListContract.UiEvent.Search(query.text))
        }

        viewModel.effect.collect {
            when (it) {
                is BreedsListContract.SideEffect.ShowToast -> {

                }
            }
        }
    }

    BreedsScreenContent(
        state = state,
        query = query,
        onQueryChange = {
            query = it
            viewModel.setEvent(BreedsListContract.UiEvent.Search(it.text))
        },
        onBreedClick = onBreedClick
    )
}


@Composable
private fun BreedsScreenContent(
    state: UiState,
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onBreedClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val indicatorHeightRatio = 0.15f

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Pretraži rase") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.text.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange(TextFieldValue("")) }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when {
                state.isLoading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                state.errorMessage != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.errorMessage ?: "Greška")
                }

                else -> LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        )
                ) {
                    items(state.breeds) { breed ->
                        BreedCard(breed = breed, onClick = { onBreedClick(breed.id) })
                    }
                }
            }
        }

        val itemCount = state.breeds.size
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        val scrollTrackHeightPx = with(density) { 300.dp.roundToPx() }
        val indicatorOffsetRatio = if (visibleItems.isNotEmpty()) {
            visibleItems.first().index.toFloat() / itemCount
        } else 0f

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = indicatorHeightRatio)
                    .offset {
                        IntOffset(x = 0, y = (indicatorOffsetRatio * scrollTrackHeightPx).toInt())
                    }
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreedCard(
    breed: Breed,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ime: ${breed.name}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (!breed.altNames.isNullOrBlank()) {
                Text(
                    text = "Alternativna imena: ${breed.altNames}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "Poreklo: ${breed.origin}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            val shortDescription = if (breed.description.length > 250)
                breed.description.substring(0, 250) + "..."
            else
                breed.description

            Text(
                text = "Opis: $shortDescription",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val temperaments = breed.temperament.split(",").map { it.trim() }.take(5)

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                temperaments.forEach { temp ->
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = temp,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    }
}

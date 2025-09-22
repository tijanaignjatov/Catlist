package com.example.projekatrma.ui.details

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.projekatrma.model.Breed

@Composable
fun DetailsScreen(
    viewModel: BreedDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.setEvent(BreedDetailsContract.UiEvent.LoadBreed)

        viewModel.effect.collect {effect ->
            when (effect) {
                is BreedDetailsContract.SideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    DetailsScreenContent(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(state: BreedDetailsContract.UiState) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val indicatorHeightRatio = 0.1f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji rase") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                state.breed != null -> {
                    val breed = state.breed
                    val imageUrl = state.imageUrl

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        item {
                            Text(
                                text = breed.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            imageUrl?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Poreklo: ${breed.origin}")
                            Text("Životni vek: ${breed.lifeSpan} godina")
                            Text("Težina: ${breed.weight.metric} kg")
                            Text("Temperament: ${breed.temperament}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Opis: ${breed.description}")
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (breed.rare == 1) "Ova rasa je retka." else "Ova rasa nije retka.",
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Text("Osobine ponašanja:", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))

                            BehaviorBar("Adaptability", breed.adaptability)
                            BehaviorBar("Affection Level", breed.affectionLevel)
                            BehaviorBar("Child Friendly", breed.childFriendly)
                            BehaviorBar("Dog Friendly", breed.dogFriendly)
                            BehaviorBar("Energy Level", breed.energyLevel)
                            BehaviorBar("Grooming", breed.grooming)
                            BehaviorBar("Health Issues", breed.healthIssues)
                            BehaviorBar("Intelligence", breed.intelligence)
                            BehaviorBar("Shedding Level", breed.sheddingLevel)
                            BehaviorBar("Social Needs", breed.socialNeeds)
                            BehaviorBar("Stranger Friendly", breed.strangerFriendly)
                            BehaviorBar("Vocalisation", breed.vocalisation)

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(onClick = {
                                breed.wikipediaUrl?.let { url ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            }) {
                                Text("Otvori Wikipedia stranicu")
                            }

                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }

                    val itemCount = 20
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

                else -> {
                    Text(
                        "Greška: ${state.errorMessage ?: "Nepoznata greška"}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun BehaviorBar(label: String, value: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        LinearProgressIndicator(
            progress = value / 5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

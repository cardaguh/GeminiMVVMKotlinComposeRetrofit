package com.example.rickandmorty.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmorty.data.models.Character
import com.example.rickandmorty.data.models.Location
import com.example.rickandmorty.data.models.Origin
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.domain.viewmodels.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onBackClick: () -> Unit,
    viewModel: CharacterViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    // Collects the character details as state from the ViewModel.
    val character by viewModel.character.collectAsState()
    // Collects the loading state as state from the ViewModel.
    val isLoading by viewModel.isLoading.collectAsState()
    // Collects the error state as state from the ViewModel.
    val error by viewModel.error.collectAsState()

    // Effect to fetch the character details when the composable is first launched or the id changes.
    LaunchedEffect(key1 = characterId) {
        viewModel.fetchCharacterById(characterId)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = character?.name ?: "Character Detail") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        if (error != null) {
            // Displays an error message if an error occurred.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error ?: "An error occurred")
            }
        } else if (isLoading) {
            // Displays a loading indicator if data is being loaded.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (character == null) {
            // Displays a message if the character is not found.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Character not found")
            }
        } else {
            // Displays the character details if there is no error, data is loaded, and the character is found.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Displays the character's image.
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character?.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Character Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                // Displays the character's name.
                Text(
                    text = character?.name ?: "Name not available",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                // Displays the character's status.
                InfoRow("Status", character?.status ?: "Status not available")
                // Displays the character's species.
                InfoRow("Species", character?.species ?: "Species not available")
                // Displays the character's gender.
                InfoRow("Gender", character?.gender ?: "Gender not available")
                // Displays the character's type.
                if (character?.type != "") {
                    InfoRow("Type", character?.type ?: "Type not available")
                }
                // Displays the character's origin.
                InfoRow("Origin", character?.origin?.name ?: "Origin not available")
                // Displays the character's location.
                InfoRow("Location", character?.location?.name ?: "Location not available")
            }
        }
    }
}

// Composable function for displaying a single row of character information.
@Composable
fun InfoRow(label: String, value: String) {
    // Row to arrange the label and value horizontally.
    Row(modifier = Modifier.fillMaxWidth()) {
        // Displays the label in bold.
        Text(text = "$label:", fontWeight = FontWeight.Bold)
        // Displays the value with some padding.
        Text(text = " $value", modifier = Modifier.padding(start = 4.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun CharacterDetailScreenPreview() {
    val mockCharacter = Character(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin = Origin("Earth", ""),
        location = Location("Earth", ""),
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episode = listOf(),
        url = "",
        created = ""
    )
    RickAndMortyTheme {
        CharacterDetailScreen(characterId = 1, onBackClick = {})
    }
}
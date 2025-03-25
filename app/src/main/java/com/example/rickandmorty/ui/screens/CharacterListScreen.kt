package com.example.rickandmorty.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.domain.viewmodels.CharacterViewModel
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.foundation.layout.height



// Composable function for displaying a list of characters.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    // Collects the list of characters as state from the ViewModel.
    val characters by viewModel.characters.collectAsState()
    // Collects the loading state as state from the ViewModel.
    val isLoading by viewModel.isLoading.collectAsState()
    // Collects the error state as state from the ViewModel.
    val error by viewModel.error.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Rick and Morty Characters") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        // Checks if there is an error.
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
        } else {
            // Displays the list of characters if there is no error and data is loaded.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(characters) { character ->
                    CharacterItem(character, onCharacterClick)
                }
            }
        }
    }
}

// Composable function for displaying a single character item.
@Composable
fun CharacterItem(character: Character, onCharacterClick: (Int) -> Unit) {
    // Creates a card to display the character details.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCharacterClick(character.id) }
    ) {
        // Column to arrange the character's image and name vertically.
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Displays the character's image.
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Character Image",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth // Change to full image
            )
            // Displays the character's name.
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

class CharacterListPreviewParameterProvider : PreviewParameterProvider<List<Character>> {
    override val values: Sequence<List<Character>> = sequenceOf(
        listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = com.example.rickandmorty.data.models.Origin("Earth", ""),
                location = com.example.rickandmorty.data.models.Location("Earth", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf(),
                url = "",
                created = ""
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = com.example.rickandmorty.data.models.Origin("Earth", ""),
                location = com.example.rickandmorty.data.models.Location("Earth", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                episode = listOf(),
                url = "",
                created = ""
            )
        )
    )
}
@Preview(showBackground = true)
@Composable
fun CharacterListScreenPreview(
    @PreviewParameter(CharacterListPreviewParameterProvider::class) characters: List<Character>
) {
    RickAndMortyTheme {
        CharacterListScreen(onCharacterClick = {})
    }
}

@Preview
@Composable
fun CharacterItemPreview(){
    RickAndMortyTheme {
        CharacterItem(
            Character(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = com.example.rickandmorty.data.models.Origin("Earth", ""),
                location = com.example.rickandmorty.data.models.Location("Earth", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                episode = listOf(),
                url = "",
                created = ""
            )
            , onCharacterClick = {})
    }
}
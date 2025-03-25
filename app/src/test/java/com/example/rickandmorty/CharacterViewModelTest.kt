package com.example.rickandmorty

import androidx.compose.ui.semantics.error
import com.example.rickandmorty.data.models.Character
import com.example.rickandmorty.data.models.CharactersResponse
import com.example.rickandmorty.data.models.Location
import com.example.rickandmorty.data.models.Origin
import com.example.rickandmorty.domain.viewmodels.CharacterViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterViewModelTest {

    private lateinit var viewModel: CharacterViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val sampleCharacter = Character(
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

    private val sampleCharactersResponse = CharactersResponse(
        listOf(sampleCharacter)
    )
    private val charactersFlow = MutableStateFlow<List<Character>>(emptyList())
    private val characterFlow = MutableStateFlow<Character?>(null)
    private val isLoadingFlow = MutableStateFlow(false)
    private val errorFlow = MutableStateFlow<String?>(null)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CharacterViewModel(FakeCharacterRepository())

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCharacters success`() = runTest {
        // Given
        viewModel.fetchCharacters()
        advanceUntilIdle()

        // Then
        val actualCharacters = viewModel.characters.first()
        assertEquals(listOf(sampleCharacter), actualCharacters)
        assertEquals(false, viewModel.isLoading.first())
        assertEquals(null, viewModel.error.first())
    }

    @Test
    fun `fetchCharacters error`() = runTest {
        // Given
        viewModel = CharacterViewModel(FakeCharacterRepositoryError())
        // When
        viewModel.fetchCharacters()
        advanceUntilIdle()
        // Then
        assertEquals("An error occurred", viewModel.error.first())
        assertEquals(false, viewModel.isLoading.first())
        assertEquals(emptyList<Character>(), viewModel.characters.first())
    }

    @Test
    fun `fetchCharacterById success`() = runTest {
        // Given
        val characterId = 1

        // When
        viewModel.fetchCharacterById(characterId)
        advanceUntilIdle()

        // Then
        val actualCharacter = viewModel.character.first()
        assertEquals(sampleCharacter, actualCharacter)
        assertEquals(false, viewModel.isLoading.first())
        assertEquals(null, viewModel.error.first())

    }
    @Test
    fun `fetchCharacterById error`()= runTest{
        //Given
        val characterId = 1
        viewModel = CharacterViewModel(FakeCharacterRepositoryError())
        //When
        viewModel.fetchCharacterById(characterId)
        advanceUntilIdle()
        //Then
        assertEquals("An error occurred",viewModel.error.first())
        assertEquals(false,viewModel.isLoading.first())
        assertEquals(null,viewModel.character.first())
    }
}

class FakeCharacterRepository : CharacterRepository {
    private val sampleCharacter = Character(
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

    private val sampleCharactersResponse = CharactersResponse(
        listOf(sampleCharacter)
    )
    override fun fetchCharacters(): Flow<CharactersResponse> {
        return flowOf(sampleCharactersResponse)
    }

    override fun fetchCharacterById(characterId: Int): Flow<Character> {
        return flowOf(sampleCharacter)
    }
}

class FakeCharacterRepositoryError : CharacterRepository {

    override fun fetchCharacters(): Flow<CharactersResponse> = flow {
        throw Exception("An error occurred")
    }

    override fun fetchCharacterById(characterId: Int): Flow<Character> = flow {
        throw Exception("An error occurred")
    }
}
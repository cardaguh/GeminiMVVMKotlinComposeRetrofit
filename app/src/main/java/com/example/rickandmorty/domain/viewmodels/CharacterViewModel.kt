package com.example.rickandmorty.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.rickandmorty.data.models.Character

// ViewModel for managing character data.
class CharacterViewModel : ViewModel() {

    // StateFlow to hold the list of characters.
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters.asStateFlow()

    // StateFlow to hold a single character's details.
    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character.asStateFlow()

    // StateFlow to track loading state.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // StateFlow to track errors.
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Initialize the ViewModel and fetch the list of characters.
    init {
        fetchCharacters()
    }

    // Fetches the list of characters from the API.
    private fun fetchCharacters() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getCharacters()
                if (response.isSuccessful) {
                    val characters = response.body()?.characters ?: emptyList()
                    _characters.value = characters
                    _error.value = null
                } else {
                    _error.value = "Error fetching characters"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    // Fetch a single character's details by ID.
    fun fetchCharacterById(characterId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getCharacterById(characterId)
                if (response.isSuccessful) {
                    _character.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Error fetching character details"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
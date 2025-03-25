package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.models.Character
import com.example.rickandmorty.data.models.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Interface to define the API endpoints.
interface ApiService {
    // Endpoint to retrieve the list of characters.
    @GET("character")
    suspend fun getCharacters(): Response<CharactersResponse>

    // Endpoint to retrieve a specific character by ID.
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") characterId: Int): Response<Character>
}
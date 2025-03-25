package com.example.rickandmorty.data.models

import com.google.gson.annotations.SerializedName

// Data class to represent a single character from the API.
data class Character(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: Origin,
    @SerializedName("location") val location: Location,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
)

// Data class to represent the origin of a character.
data class Origin(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

// Data class to represent the location of a character.
data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

// Data class to represent the response with the list of characters.
data class CharactersResponse(
    @SerializedName("results") val characters: List<Character>
)

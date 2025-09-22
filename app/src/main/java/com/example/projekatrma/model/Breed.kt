package com.example.projekatrma.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Breed(
    val id: String,
    val name: String,
    @SerialName("alt_names") val altNames: String? = null,
    val description: String,
    val temperament: String,
    val origin: String,
    @SerialName("life_span") val lifeSpan: String,
    val weight: Weight,
    val adaptability: Int,
    @SerialName("affection_level") val affectionLevel: Int,
    @SerialName("child_friendly") val childFriendly: Int,
    @SerialName("dog_friendly") val dogFriendly: Int,
    @SerialName("energy_level") val energyLevel: Int,
    val grooming: Int,
    @SerialName("health_issues") val healthIssues: Int,
    val intelligence: Int,
    @SerialName("shedding_level") val sheddingLevel: Int,
    @SerialName("social_needs") val socialNeeds: Int,
    @SerialName("stranger_friendly") val strangerFriendly: Int,
    val vocalisation: Int,
    @SerialName("wikipedia_url") val wikipediaUrl: String? = null,
    @SerialName("rare") val rare: Int
)


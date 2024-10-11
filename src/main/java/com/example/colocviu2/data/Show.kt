package com.example.colocviu2.data

import java.io.Serializable

data class Show(
    val id: Int,
    val url: String,
    val name: String,
    val season: Int,
    val number: Int,
    val type: String,
    val airdate: String,
    val airtime: String,
    val airstamp: String,
    val runtime: Int,
    val rating: Rating,
    val image: Image?,
    val summary: String,
    val links: Links,
    val embedded: Embedded
): Serializable

data class Rating(
    val average: Double?
):Serializable

data class Image(
    val medium: String?,
    val original: String?
):Serializable

data class Links(
    val self: Link,
    val show: Link
):Serializable

data class Link(
    val href: String
):Serializable

data class Embedded(
    val show: ShowDetails
) :Serializable

data class ShowDetails(
    val id: Int,
    val url: String,
    val name: String,
    val type: String,
    val language: String,
    val genres: List<String>,
    val status: String,
    val runtime: Int,
    val averageRuntime: Int,
    val premiered: String,
    val ended: String,
    val officialSite: String,
    val schedule: Schedule,
    val rating: Rating,
    val weight: Int,
    val network: Network,
    val webChannel: WebChannel,
    val externals: Externals,
    val image: Image,
    val summary: String,
    val updated: Long,
    val links: ShowLinks
) :Serializable

data class Schedule(
    val time: String,
    val days: List<String>
): Serializable

data class Network(
    val id: Int,
    val name: String,
    val country: Country,
    val officialSite: String
) : Serializable

data class WebChannel(
    val id: Int,
    val name: String,
    val country: Country,
    val officialSite: String
):Serializable

data class Country(
    val name: String,
    val code: String,
    val timezone: String
):Serializable

data class Externals(
    val tvrage: Any?, // Can be null
    val thetvdb: Int,
    val imdb: String
): Serializable

data class ShowLinks(
    val self: Link,
    val previousepisode: Link
): Serializable

package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MasterTownApi(
    @Json(name = "id") val id: String,
    @Json(name = "id_old") val idOld: String?,
    @Json(name = "url") val url: String,
    @Json(name = "nombre") val name: String,
    @Json(name = "capital") val capitalCity: String,
    @Json(name = "zona_comarcal") val countyArea: String,
    @Json(name = "num_hab") val population: Int,
    @Json(name = "destacada") val highlighted: Int,
    @Json(name = "latitud") val latitude: String,
    @Json(name = "latitud_dec") val latitudeDec: Float,
    @Json(name = "longitud") val longitude: String,
    @Json(name = "longitud_dec") val longitudeDec: Float,
    @Json(name = "altitud") val altitude: Int,
)
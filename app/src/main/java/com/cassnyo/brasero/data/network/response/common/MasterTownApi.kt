package com.cassnyo.brasero.data.network.response.common

import com.cassnyo.brasero.ui.model.MasterTown
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MasterTownApi(
    @Json(name = "id") val id: String,
    @Json(name = "id_old") val idOld: String?,
    @Json(name = "url") val url: String,
    @Json(name = "nombre") val name: String,
    @Json(name = "capital") val capital: String,
    @Json(name = "zona_comarcal") val zone: String,
    @Json(name = "num_hab") val population: String,
    @Json(name = "latitud") val latitude: String,
    @Json(name = "latitud_dec") val latitudeDec: String,
    @Json(name = "longitud") val longitude: String,
    @Json(name = "longitud_dec") val longitudeDec: String,
    @Json(name = "destacada") val highlighted: String
) {
    fun toMasterTown(): MasterTown {
        return MasterTown(this.id.removePrefix("id"), this.name)
    }
}
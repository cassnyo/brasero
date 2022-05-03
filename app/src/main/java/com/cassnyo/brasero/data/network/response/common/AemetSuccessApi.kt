package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AemetSuccessApi(
    @Json(name = "descripcion") val description: String,
    @Json(name = "estado") val status: Int,
    @Json(name = "datos") val data: String,
    @Json(name = "metadatos") val metadata: String
)
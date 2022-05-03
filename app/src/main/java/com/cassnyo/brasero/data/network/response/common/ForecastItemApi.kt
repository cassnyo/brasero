package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastItemApi<T>(
    @Json(name = "dia") val day: List<T>
)
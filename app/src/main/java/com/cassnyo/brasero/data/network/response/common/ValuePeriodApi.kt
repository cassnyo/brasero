package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValuePeriodApi<T>(
    @Json(name = "value") val value: T,
    @Json(name = "periodo") val period: String?,
)
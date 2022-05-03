package com.cassnyo.brasero.data.network.response.hourly

import com.cassnyo.brasero.data.network.response.common.SkyStatusApi
import com.cassnyo.brasero.data.network.response.common.ValuePeriodApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.LocalTime

@JsonClass(generateAdapter = true)
data class HourlyForecastItemDayApi(
    @Json(name = "fecha") val date: LocalDateTime,
    @Json(name = "orto") val sunrise: LocalTime,
    @Json(name = "ocaso") val sunset: LocalTime,
    @Json(name = "estadoCielo") val skyStatus: List<SkyStatusApi>,
    @Json(name = "precipitacion") val rain: List<ValuePeriodApi<String>>,
    @Json(name = "probPrecipitacion") val chanceOfRain: List<ValuePeriodApi<String>>,
    @Json(name = "probTormenta") val chanceOfStorm: List<ValuePeriodApi<String>>,
    @Json(name = "nieve") val snow: List<ValuePeriodApi<String>>,
    @Json(name = "probNieve") val chanceOfSnow: List<ValuePeriodApi<String>>,
    @Json(name = "temperatura") val temperature: List<ValuePeriodApi<String>>,
    @Json(name = "sensTermica") val windchill: List<ValuePeriodApi<String>>,
    @Json(name = "humedadRelativa") val relativeHumidity: List<ValuePeriodApi<String>>,
    @Json(name = "vientoAndRachaMax") val wind: List<HourlyWindApi>
)
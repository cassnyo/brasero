package com.cassnyo.brasero.data.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter {

    @ToJson
    fun toJson(localDateTime: LocalDateTime): String {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @FromJson
    fun fromJson(localDateTime: String): LocalDateTime {
        return LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

}
package com.cassnyo.brasero.data.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {

    @TypeConverter
    fun toString(value: LocalDate?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }

}
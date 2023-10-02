package com.cassnyo.brasero.common.extension

import java.time.LocalDate

fun LocalDate.isToday(): Boolean = this == LocalDate.now()

fun LocalDate.isTomorrow(): Boolean = this == LocalDate.now().plusDays(1)

fun LocalDate.isTodayOrAfter(): Boolean {
    val today = LocalDate.now()
    return this == today || this.isAfter(today)
}
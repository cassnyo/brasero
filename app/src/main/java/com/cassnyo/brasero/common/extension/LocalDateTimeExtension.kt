package com.cassnyo.brasero.common.extension

import java.time.LocalDateTime

fun LocalDateTime.isWithinNext24Hours(): Boolean {
    val now = LocalDateTime.now()
    val start = now.minusHours(1)
    val end = now.plusDays(1)
    return isAfter(start) && isBefore(end)
}
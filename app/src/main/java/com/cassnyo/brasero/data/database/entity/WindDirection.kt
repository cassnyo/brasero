package com.cassnyo.brasero.data.database.entity

sealed class WindDirection {
    object North: WindDirection()
    object NorthEast: WindDirection()
    object East: WindDirection()
    object SouthEast: WindDirection()
    object South: WindDirection()
    object SouthWest: WindDirection()
    object West: WindDirection()
    object NorthWest: WindDirection()
}
package com.cassnyo.brasero.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cassnyo.brasero.R

val Poppins = FontFamily(
    // Thin
    Font(R.font.poppins_thin, weight = FontWeight.Thin),
    Font(R.font.poppins_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),

    // Extra light
    Font(R.font.poppins_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.poppins_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),

    // Light
    Font(R.font.poppins_light, weight = FontWeight.Light),
    Font(R.font.poppins_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),

    // Regular
    Font(R.font.poppins_regular),

    // Italic
    Font(R.font.poppins_italic, style = FontStyle.Italic),

    // Medium
    Font(R.font.poppins_medium, weight = FontWeight.Medium),
    Font(R.font.poppins_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),

    // Semi bold
    Font(R.font.poppins_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.poppins_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),

    // Bold
    Font(R.font.poppins_bold, weight = FontWeight.Bold),
    Font(R.font.poppins_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),

    // Extra bold
    Font(R.font.poppins_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.poppins_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),

    // Black
    Font(R.font.poppins_black, weight = FontWeight.Black),
    Font(R.font.poppins_black_italic, weight = FontWeight.Black, style = FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Light,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp
    ),
    h5 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
)
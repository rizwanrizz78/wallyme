package io.wally.me.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val NeonBlue = Color(0xFF00FFFF)
val NeonPurple = Color(0xFF9D00FF)
val NeonPink = Color(0xFFFF00FF)
val DeepSpaceBlack = Color(0xFF0A0A12)
val DarkSurface = Color(0xFF1E1E2C)
val LightText = Color(0xFFEEEEEE)
val GrayText = Color(0xFFAAAAAA)
val GlassWhite = Color(0x22FFFFFF)

val NeonGradient = Brush.linearGradient(
    colors = listOf(NeonBlue, NeonPurple, NeonPink)
)

val CardGradient = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color.Black)
)

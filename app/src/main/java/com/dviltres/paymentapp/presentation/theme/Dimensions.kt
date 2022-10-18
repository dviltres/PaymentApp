package com.dviltres.paymentapp.presentation.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val default: Dp = 0.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceFive: Dp = 5.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceTen: Dp = 10.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceMediumLarge: Dp = 55.dp,
    val spaceExtraLarge: Dp = 64.dp,
    val spaceHuge: Dp = 70.dp,
    val IconSizeSmall: Dp = 15.dp,
    val IconSizeMedium: Dp = 25.dp,
    val IconSizeLarge: Dp = 35.dp
)

val LocalSpacing = compositionLocalOf { Dimensions() }

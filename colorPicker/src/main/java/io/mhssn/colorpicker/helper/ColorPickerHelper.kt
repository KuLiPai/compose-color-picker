package io.mhssn.colorpicker.helper

import androidx.compose.ui.graphics.Color
import io.mhssn.colorpicker.data.ColorRange
import kotlin.math.pow


internal object ColorPickerHelper {
    fun calculateRangeProgress(progress: Double): Pair<Double, ColorRange> {
        val range: ColorRange
        return progress * 6 - when {
            progress < 1f / 6 -> {
                range = ColorRange.RedToYellow
                0
            }
            progress < 2f / 6 -> {
                range = ColorRange.YellowToGreen
                1
            }
            progress < 3f / 6 -> {
                range = ColorRange.GreenToCyan
                2
            }
            progress < 4f / 6 -> {
                range = ColorRange.CyanToBlue
                3
            }
            progress < 5f / 6 -> {
                range = ColorRange.BlueToPurple
                4
            }
            else -> {
                range = ColorRange.PurpleToRed
                5
            }
        } to range
    }
}


fun Color.luminance(): Double {
    fun channel(c: Float): Double {
        val v = c / 255.0
        return if (v <= 0.03928) v / 12.92 else ((v + 0.055) / 1.055).pow(2.4)
    }
    return 0.2126 * channel(red * 255) +
            0.7152 * channel(green * 255) +
            0.0722 * channel(blue * 255)
}

fun contrastRatio(l1: Double, l2: Double): Double {
    val (light, dark) = if (l1 > l2) l1 to l2 else l2 to l1
    return (light + 0.05) / (dark + 0.05)
}

/** 返回适合做前景的颜色 (通常是黑或白)，保证最优对比度 */
fun Color.onColor(): Color {
    val bgL = luminance()
    val whiteContrast = contrastRatio(bgL, Color.White.luminance())
    val blackContrast = contrastRatio(bgL, Color.Black.luminance())
    return if (whiteContrast >= blackContrast) Color.White else Color.Black
}

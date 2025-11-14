package com.example.helloworld.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.helloworld.R

sealed class UiClassification(
    @StringRes val textRes: Int,
    @ColorRes val colorRes: Int,
    val range: ClosedFloatingPointRange<Float>
) {
   object Underweight : UiClassification(
       R.string.imc_classification_underweight,
       R.color.imc_blue_underweight,
       0f..18.5f
   )
   object Normal : UiClassification(
       R.string.imc_classification_normal,
       R.color.imc_green_normal,
       18.5f..25f
   )
   object Overweight : UiClassification(
       R.string.imc_classification_overweight,
       R.color.imc_yellow_overweight,
       25f..30f
   )
   object ObesityI : UiClassification(
       R.string.imc_classification_obesity_i,
       R.color.imc_orange_obesity_i,
       30f..35f
   )
   object ObesityII : UiClassification(
       R.string.imc_classification_obesity_ii,
       R.color.imc_red_obesity_ii,
       35f..40f
   )
   object ObesityIII : UiClassification(
       R.string.imc_classification_obesity_iii,
       R.color.imc_dark_red_obesity_iii,
       40f..Float.MAX_VALUE // De 40 até o infinito
   )
   object Unknown : UiClassification(
       R.string.imc_classification_unknown,
       R.color.imc_gray_unknown,
       -Float.MAX_VALUE..0f // Valores negativos ou zero
   )
}
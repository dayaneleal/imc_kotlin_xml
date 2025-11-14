package com.example.helloworld.presentation.state

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class ImcUiState(
    val imc: Float,
    @StringRes val classificationTextRes: Int,
    @ColorRes val classificationColorRes: Int,
    val indicatorPosition: Float
)
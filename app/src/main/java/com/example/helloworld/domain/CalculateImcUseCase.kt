package com.example.helloworld.domain

import kotlin.math.pow

class CalculateImcUseCase {
    fun execute(weight: Float, height: Float): Float {
        return weight / (height / 100).pow(2)
    }
}
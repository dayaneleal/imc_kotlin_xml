package com.example.helloworld.presentation.mapper

import com.example.helloworld.presentation.model.ImcBarSegment
import com.example.helloworld.presentation.state.ImcUiState
import com.example.helloworld.presentation.model.UiClassification

class ImcResultMapper {
    private val minImcVisual = 13.5f
    private val maxImcVisual = 45f
    private val imcVisualRange = maxImcVisual - minImcVisual

    private val classifications = listOf(
        UiClassification.Underweight,
        UiClassification.Normal,
        UiClassification.Overweight,
        UiClassification.ObesityI,
        UiClassification.ObesityII,
        UiClassification.ObesityIII,
        UiClassification.Unknown
    )
    private fun getClassificationFor(imc: Float): UiClassification {
        return classifications.first { imc in it.range }
    }

    fun map(imc: Float): ImcUiState {
        val classification = getClassificationFor(imc)

        val constrainedImc = imc.coerceIn(minImcVisual, maxImcVisual)
        val position = (constrainedImc - minImcVisual) / imcVisualRange

        return ImcUiState(
            imc = imc,
            classificationTextRes = classification.textRes,
            classificationColorRes = classification.colorRes,
            indicatorPosition = position
        )
    }

    fun getImcBarSegments(): List<ImcBarSegment> {
        return classifications
            .filter { it !is UiClassification.Unknown }
            .map { classification ->
                val segmentStart = maxOf(classification.range.start, minImcVisual)
                val segmentEnd = minOf(classification.range.endInclusive, maxImcVisual)
                val imcSpan = segmentEnd - segmentStart

                ImcBarSegment(
                    weight = if (imcSpan > 0) imcSpan / imcVisualRange else 0f,
                    colorRes = classification.colorRes
                )
            }.filter { it.weight > 0f }
    }
}
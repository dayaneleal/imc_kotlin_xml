package com.example.helloworld.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helloworld.domain.CalculateImcUseCase
import com.example.helloworld.presentation.model.ImcBarSegment
import com.example.helloworld.presentation.mapper.ImcResultMapper
import com.example.helloworld.presentation.state.ImcUiState
import com.example.helloworld.presentation.model.InputError
import com.example.helloworld.presentation.state.ValidationState

class ImcViewModel : ViewModel() {

    private val calculateImcUseCase = CalculateImcUseCase()

    private val _uiState = MutableLiveData<ImcUiState>()
    val uiState: LiveData<ImcUiState> = _uiState

    private val _validationError = MutableLiveData<Event<ValidationState>>()
    val validationError: LiveData<Event<ValidationState>> = _validationError
    private val imcResultMapper = ImcResultMapper()

    fun getImcBarSegments(): List<ImcBarSegment> {
        return imcResultMapper.getImcBarSegments()
    }

    fun calculateImc(weightStr: String?, heightStr: String?) {

        val weight = weightStr?.toFloatOrNull()
        val height = heightStr?.toFloatOrNull()
        val weightError = validateInput(weight)
        val heightError = validateInput( height)


        if (weightError != null || heightError != null) {
            _validationError.value = Event(
                ValidationState(
                    weightError = weightError,
                    heightError = heightError
                )
            )
            return
        }

        val result = calculateImcUseCase.execute(weight!!, height!!)
        val uiState = ImcResultMapper().map(result)

        _uiState.value = uiState
    }

    private fun validateInput(value: Float?): InputError? {

        return when {
            value == null -> InputError.Invalid
            value <= 0f -> InputError.NotPositive
            else -> null
        }
    }

    fun resetState() {
        _uiState.postValue(null)
    }
}

open class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
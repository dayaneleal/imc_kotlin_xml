package com.example.helloworld.presentation.model

sealed class InputError {
    object Invalid : InputError()
    object NotPositive : InputError()
}

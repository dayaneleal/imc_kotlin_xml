package com.example.helloworld.presentation.state

import com.example.helloworld.presentation.model.InputError

data class ValidationState(
    val weightError: InputError? = null,
    val heightError: InputError? = null
)
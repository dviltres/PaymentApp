package com.dviltres.paymentapp.presentation.util.uiEvent

sealed class UiEvent {
    data class Success(val data: Any? = null): UiEvent()
    object NavigateUp: UiEvent()
    data class ShowSnackbar(val message: UiText): UiEvent()
}

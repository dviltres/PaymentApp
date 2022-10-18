package com.dviltres.paymentapp.presentation.paymentMethod

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.useCase.paymentMethod.PaymentMethodUseCases
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    private val paymentMethodUseCases: PaymentMethodUseCases
): ViewModel() {

    var state by mutableStateOf(PaymentMethodState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getPaymentMethods()
    }

    fun onEvent(event: PaymentMethodEvent) {
        when(event) {
            is PaymentMethodEvent.OnChangeScrollPosition -> {}
            PaymentMethodEvent.OnConfirmClick -> {}
            PaymentMethodEvent.OnRefresh -> {}
            is PaymentMethodEvent.OnSelectPaymentMethod -> {
                state = state.copy(
                    selectedPaymentMethod = event.paymentMethod,
                    expanded = !state.expanded
                )
            }
            PaymentMethodEvent.OnTriggerNextPage -> {}
            is PaymentMethodEvent.OnUpdateSearchState -> {}
            is PaymentMethodEvent.OnUpdateSearchText -> {}
        }
    }


    private fun getPaymentMethods() {
        viewModelScope.launch {
            paymentMethodUseCases.getPaymentMethods(state.query).collect { result->
                when(result){
                    is Resource.Success -> {
                        state = state.copy(
                            paymentMethods = result.data!!,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _uiEvent.send(
                            UiEvent.ShowSnackbar(message = UiText.DynamicString(result.message!!))
                        )
                    }
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                }
            }
        }
    }
}
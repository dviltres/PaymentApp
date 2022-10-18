package com.dviltres.paymentapp.presentation.payment

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.repository.PaymentRepository
import com.dviltres.paymentapp.domain.useCase.payment.PaymentUseCases
import com.dviltres.paymentapp.domain.useCase.util.FilterOutDigits
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {

    var state by mutableStateOf(PaymentState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
       getPayments()
    }

    fun onEvent(event: PaymentEvent) {
        when(event) {
            PaymentEvent.OnAddPaymentClick -> {
                state = state.copy(
                    expanded = !state.expanded
                )
            }
            is PaymentEvent.OnChangeScrollPosition -> TODO()
            PaymentEvent.OnConfirmClick -> TODO()
            is PaymentEvent.OnPaymentChange -> {
                state = state.copy(
                    amount = filterOutDigits(event.amount)
                )
            }
            PaymentEvent.OnRefresh -> TODO()
            PaymentEvent.OnTriggerNextPage -> TODO()
            is PaymentEvent.OnUpdateSearchState -> TODO()
            is PaymentEvent.OnUpdateSearchText -> TODO()
            PaymentEvent.OnCloseAddPaymentView -> {
                state = state.copy(
                    expanded = false,
                    amount = ""
                )
            }
        }
    }

    private fun getPayments() {
        viewModelScope.launch {
            paymentUseCases.getPayments(state.query).collect { result->
                when(result){
                    is Resource.Success -> {
                        state = state.copy(
                            payments = result.data!!,
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
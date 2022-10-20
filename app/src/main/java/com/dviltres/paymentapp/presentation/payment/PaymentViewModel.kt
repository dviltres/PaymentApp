package com.dviltres.paymentapp.presentation.payment

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.useCase.payment.PaymentUseCases
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dviltres.paymentapp.R

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
    private val context: Application,
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
            PaymentEvent.OnAddAmountClick -> {
                state = state.copy(
                    expanded = !state.expanded
                )
            }
            is PaymentEvent.OnAmountChange -> {
                state = state.copy(
                    amount = event.amount
                )
            }
            PaymentEvent.OnRefresh -> {
                getPayments()
            }
            PaymentEvent.OnCloseAddAmountView -> {
                state = state.copy(
                    expanded = false
                )
            }
            PaymentEvent.OnConfirmClick -> {
                goToPaymentMethodScreen()
            }
        }
    }

    private fun goToPaymentMethodScreen(){
        viewModelScope.launch {
           if(state.amount.toDouble() > 0){
               _uiEvent.send(UiEvent.Success(state.amount))
            }
            else
                Toast.makeText(context, UiText.StringResource(R.string.invalid_amount).asString(context),Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPayments() {
        viewModelScope.launch {
            paymentUseCases.getPayments().collect { result->
                when(result){
                    is Resource.Success -> {
                       result.data?.let {
                           state = state.copy(
                               payments = it,
                               isLoading = false
                           )
                       }
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false
                        )
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
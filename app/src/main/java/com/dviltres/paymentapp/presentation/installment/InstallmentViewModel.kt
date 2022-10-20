package com.dviltres.paymentapp.presentation.installment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.useCase.installment.InstallmentUseCases
import com.dviltres.paymentapp.domain.useCase.paymentMethod.PaymentMethodUseCases
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.domain.useCase.cardIssuer.CardIssuerUseCases
import com.dviltres.paymentapp.domain.useCase.payment.PaymentUseCases

@HiltViewModel
class InstallmentViewModel @Inject constructor(
    private val installmentUseCases: InstallmentUseCases,
    private val paymentMethodUseCases:PaymentMethodUseCases,
    private val cardIssuerUseCases: CardIssuerUseCases,
    private val paymentUseCases: PaymentUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(InstallmentState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("payment_method_id")?.let { paymentMethodId ->
                state = state.copy(
                    paymentMethodId = paymentMethodId
                )
                getPaymentMethod(paymentMethodId)
            }
            savedStateHandle.get<String>("issuer_id")?.let { issuer_id ->
                state = state.copy(
                    issuerId = issuer_id
                )
                getCardIssuers(issuer_id)
            }
            savedStateHandle.get<String>("amount")?.let { amount ->
                state = state.copy(
                    amount = amount
                )
            }
        }
        if(state.paymentMethodId != null && state.issuerId != null && state.amount != null){
            var updatedPayment by mutableStateOf(state.payment)
            updatedPayment = updatedPayment.copy(
                amount = state.amount.toDouble()
            )
            state = state.copy(
                payment = updatedPayment
            )
            getInstallments()
        }
        else
            viewModelScope.launch {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(message = UiText.StringResource(R.string.unexpectedError))
                )
            }
    }

    fun onEvent(event: InstallmentEvent) {
        when(event) {
            InstallmentEvent.OnPaymentConfirmClick -> {
                paymentConfirm()
            }
            is InstallmentEvent.OnSelectInstallment -> {
                var updatePayment by mutableStateOf(state.payment)
                updatePayment = updatePayment.copy(
                    installment = event.installment
                )
                state = state.copy(
                    selectedInstallment = event.installment,
                    payment = updatePayment
                )
            }
            is InstallmentEvent.OnShowErrorMessage -> {

            }
            InstallmentEvent.OnDismissRequest -> {
                state = state.copy(
                    installmentExpanded = false
                )
            }
            is InstallmentEvent.OnGloballyPositioned -> {
                state = state.copy(
                    selectedInstallmentMessageSize = event.size
                )
            }
            InstallmentEvent.OnInstallmentClick -> {
                state = state.copy(
                    installmentExpanded = !state.installmentExpanded
                )
            }
            is InstallmentEvent.OnValueChangeInstallment -> {
                var updatePayment by mutableStateOf(state.payment)
                updatePayment = updatePayment.copy(
                   installment = event.installment
                )
                state = state.copy(
                    selectedInstallment = event.installment,
                    payment = updatePayment
                )
            }
            is InstallmentEvent.OnCVCValueChange -> {
                var updateCreditCard by mutableStateOf(state.creditCard)
                updateCreditCard = updateCreditCard.copy(
                    cvc = event.value
                )
                state = state.copy(creditCard = updateCreditCard)
            }
            is InstallmentEvent.OnDropdownMenuFocusChanged -> {

            }
            is InstallmentEvent.OnExpirationValueChange -> {
                val expiration = if (event.value.length >= 4)
                    event.value.substring(0..3)
                else
                    event.value

                var updateCreditCard by mutableStateOf(state.creditCard)
                updateCreditCard = updateCreditCard.copy(
                    expiration = expiration
                )
                state = state.copy(creditCard = updateCreditCard)
            }
            is InstallmentEvent.OnFocusChanged -> {

            }
            is InstallmentEvent.OnNameValueChange -> {
                var updateCreditCard by mutableStateOf(state.creditCard)
                updateCreditCard = updateCreditCard.copy(
                    holderName = event.value
                )
                state = state.copy(creditCard = updateCreditCard)
            }
            is InstallmentEvent.OnNumberValueChange -> {
                val number = if (event.value.length >= 16)
                    event.value.substring(0..15)
                else
                    event.value

                var updateCreditCard by mutableStateOf(state.creditCard)
                updateCreditCard = updateCreditCard.copy(
                    number = number
                )
                state = state.copy(creditCard = updateCreditCard)
            }
        }
    }


    private fun getInstallments() {
        viewModelScope.launch {
            installmentUseCases.getInstallment(
                amount = state.amount!!,
                paymentMethodId = state.paymentMethodId!!,
                issuerId = state.issuerId!!
            ).collect { result->
                when(result){
                    is Resource.Success -> {
                        result.data?.let {
                            state = state.copy(
                                installment = it,
                                recommendedMessage = it.recommended_message,
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

    private fun getPaymentMethod(paymentMethodId: String) {
        viewModelScope.launch {
            paymentMethodUseCases.getPaymentMethodById(paymentMethodId = paymentMethodId).collect { result->
                when(result){
                    is Resource.Success -> {
                        result.data?.let {
                            var updatedPayment by mutableStateOf(state.payment)
                            updatedPayment = updatedPayment.copy(
                                paymentMethod = it
                            )
                            state = state.copy(
                                isLoading = false,
                                payment = updatedPayment
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

    private fun getCardIssuers(cardIssuerId: String) {
        viewModelScope.launch {
            cardIssuerUseCases.getCardIssuerById(cardIssuerId).collect { result->
                when(result){
                    is Resource.Success -> {
                        result.data?.let {
                            var updatedPayment by mutableStateOf(state.payment)
                            var updatedCreditCard by mutableStateOf(state.creditCard)
                            updatedPayment = updatedPayment.copy(
                                cardIssuer = it
                            )
                            updatedCreditCard = updatedCreditCard.copy(
                                logoCardIssuer = it.thumbnail
                            )
                            state = state.copy(
                                payment = updatedPayment,
                                creditCard = updatedCreditCard,
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

    private fun paymentConfirm(){
            viewModelScope.launch {
                paymentUseCases.paymentConfirm(
                    card = state.creditCard,
                    payment = state.payment
                ).collect { result->
                    when(result){
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(
                                    payment = it,
                                    isLoading = false
                                )
                                _uiEvent.send(UiEvent.Success())
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
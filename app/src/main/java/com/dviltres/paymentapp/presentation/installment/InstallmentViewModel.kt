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

@HiltViewModel
class InstallmentViewModel @Inject constructor(
    private val installmentUseCases: InstallmentUseCases,
    private val paymentMethodUseCases:PaymentMethodUseCases,
    private val cardIssuerUseCases: CardIssuerUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(InstallmentState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
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
        savedStateHandle.get<Int>("amount")?.let { amount ->
            state = state.copy(
                amount = amount
            )
        }

        if(state.paymentMethodId != null && state.issuerId != null && state.amount != null)
            getInstallments()
        else
            viewModelScope.launch {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(message = UiText.StringResource(R.string.unexpectedError))
                )
            }
    }

    fun onEvent(event: InstallmentEvent) {
        when(event) {
            InstallmentEvent.OnConfirmClick -> {}
            InstallmentEvent.OnRefresh -> {}
            is InstallmentEvent.OnSelectInstallment -> {
                state = state.copy(
                    selectedInstallment = event.installment
                )
            }
            is InstallmentEvent.OnShowErrorMessage -> TODO()
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
                        state = state.copy(
                            installment = result.data!!,
                            recommendedMessage = result.data.recommended_message,
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

    private fun getPaymentMethod(paymentMethodId: String) {
        viewModelScope.launch {
            paymentMethodUseCases.getPaymentMethodById(paymentMethodId = paymentMethodId).collect { result->
                when(result){
                    is Resource.Success -> {
                        state = state.copy(
                            paymentMethod = result.data!!,
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

    private fun getCardIssuers(cardIssuerId: String) {
        viewModelScope.launch {
            cardIssuerUseCases.getCardIssuerById(cardIssuerId).collect { result->
                when(result){
                    is Resource.Success -> {
                        state = state.copy(
                            cardIssuer = result.data!!,
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
package com.dviltres.paymentapp.presentation.cardIssuer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.useCase.cardIssuer.CardIssuerUseCases
import com.dviltres.paymentapp.domain.useCase.paymentMethod.PaymentMethodUseCases
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardIssuerViewModel @Inject constructor(
    private val cardIssuerUseCases: CardIssuerUseCases,
    private val paymentMethodUseCases: PaymentMethodUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(CardIssuerState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<String>("payment_method_id")?.let { paymentMethodId ->
            getPaymentMethod(paymentMethodId)
        }
    }

    fun onEvent(event: CardIssuerEvent) {
        when(event) {
            is CardIssuerEvent.OnChangeScrollPosition -> {}
            CardIssuerEvent.OnConfirmClick -> {}
            CardIssuerEvent.OnRefresh -> {}
            is CardIssuerEvent.OnSelectPaymentMethod -> {
                state = state.copy(
                    selectedCardIssuer = event.cardIssuer,
                    expanded = !state.expanded
                )
            }
            CardIssuerEvent.OnTriggerNextPage -> {}
            is CardIssuerEvent.OnUpdateSearchState -> {}
            is CardIssuerEvent.OnUpdateSearchText -> {}
        }
    }


    private fun getCardIssuers(paymentMethodId:String) {
        viewModelScope.launch {
            cardIssuerUseCases.getCardIssuers(query = state.query, paymentMethodId = paymentMethodId).collect { result->
                when(result){
                    is Resource.Success -> {
                       result.data?.let {
                           state = state.copy(
                               cardIssuers = it,
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
                            state = state.copy(
                                paymentMethod = it,
                                isLoading = false
                            )
                        }
                        getCardIssuers(paymentMethodId)
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
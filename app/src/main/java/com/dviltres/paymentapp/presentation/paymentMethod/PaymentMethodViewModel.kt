package com.dviltres.paymentapp.presentation.paymentMethod

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dviltres.paymentapp.data.common.Constants
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.domain.useCase.paymentMethod.PaymentMethodUseCases
import com.dviltres.paymentapp.presentation.components.SearchState
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    private val paymentMethodUseCases: PaymentMethodUseCases,
    private val savedStateHandle: SavedStateHandle,
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
            is PaymentMethodEvent.OnChangeScrollPosition -> {
                setListScrollPosition(event.position)
            }
            PaymentMethodEvent.OnConfirmClick -> {

            }
            PaymentMethodEvent.OnRefresh -> {
                getPaymentMethods()
            }
            is PaymentMethodEvent.OnSelectPaymentMethod -> {
                state = state.copy(
                    selectedPaymentMethod = event.paymentMethod,
                    expanded = !state.expanded
                )
            }
            PaymentMethodEvent.OnTriggerNextPage -> {
                nextPage()
            }
            is PaymentMethodEvent.OnUpdateSearchState -> {
                state = state.copy(searchState = event.searchState)
            }
            is PaymentMethodEvent.OnUpdateSearchText -> {
                state = state.copy(query = event.query)
            }
            is PaymentMethodEvent.OnShowErrorMessage -> {

            }
            is PaymentMethodEvent.OnUpdateScrollPosition -> {
                if (event.newScrollIndex == state.lastScrollIndex) return
                state = state.copy(
                    lastScrollIndex = event.newScrollIndex
                )
            }
            is PaymentMethodEvent.OnSearchClicked -> {
                resetSearchState()
                getPaymentMethods()
            }
        }
    }

    private fun getPaymentMethods() {
        viewModelScope.launch {
            paymentMethodUseCases.getPaymentMethods(
                query = state.query,
                page = state.page
            ).collect { result->
                when(result){
                    is Resource.Success -> {
                        state = state.copy(
                            isLoading = false
                        )
                        result.data?.let {
                            appendItems(it)
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

    private fun appendItems(items: List<PaymentMethod>){
        val current = ArrayList(state.paymentMethods)
        current.addAll(items)
        state = state.copy(
            paymentMethods = current
        )
    }

    private fun setListScrollPosition(position: Int){
        state = state.copy(
            listScrollPosition = position
        )
        savedStateHandle[Constants.STATE_KEY_LIST_POSITION] = position
    }

    private fun nextPage() {
        if ((state.listScrollPosition + 1) >= (state.page * Constants.PAGE_SIZE)) {
            incrementPage()
            if (state.page > 1) {
                getPaymentMethods()
            }
        }
    }

    private fun incrementPage(){
        setPage(state.page + 1)
    }

    private fun setPage(page: Int){
        state = state.copy(
            page = page
        )
        savedStateHandle[Constants.STATE_KEY_LIST_POSITION] = page
    }

    private fun resetSearchState() {
        state = state.copy(
            paymentMethods = listOf(),
            page = 1,
        )
        setListScrollPosition(0)
    }
}
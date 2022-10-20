package com.dviltres.paymentapp.presentation.installment

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.presentation.components.CreditCard
import com.dviltres.paymentapp.presentation.components.CreditCardForm
import com.dviltres.paymentapp.presentation.components.DefaultAppBar
import com.dviltres.paymentapp.presentation.components.RoundedButton
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.presentation.util.navigation.Screen
import com.dviltres.paymentapp.presentation.util.navigation.StandardScaffold
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText

@Composable
fun InstallmentScreen(
    paymentMethodId:String,
    amount:String,
    issuerId:String,
    navController: NavController,
    viewModel: InstallmentViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = UiText.StringResource(R.string.success_payment).asString(context)
                    )
                    navController.navigate(Screen.PaymentScreen.route)
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
            }
        }
    }

   StandardScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier,
            drawerGesturesEnabled = false,
            topBar = {
                DefaultAppBar(
                    title = stringResource(R.string.finalize_payment),
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(Screen.CardIssuerScreen.route +"?payment_method_id=${paymentMethodId}&amount=${amount}")
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(
                                id = R.string.backIcon))
                        }
                    }
                )
            },
            bottomBar = {
                   RoundedButton(
                       text = if(state.selectedInstallment.isNullOrBlank())
                           stringResource(R.string.finalize_payment)
                       else
                           state.selectedInstallment,
                       isEnabled = true,
                       displayProgressBar = state.isLoading,
                       onClick = {
                          viewModel.onEvent(InstallmentEvent.OnPaymentConfirmClick)
                       },
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(spacing.spaceMedium)
                   )
            }
        ) {
       Column(
           modifier = Modifier.scrollable(
               state = scrollState,
               orientation = Orientation.Vertical
           ).padding(spacing.spaceMedium)
       ){
           CreditCard(
               number = state.creditCard.number,
               expiration = state.creditCard.expiration,
               holderName = state.creditCard.holderName,
               cvc = state.creditCard.cvc,
               emptyChar = 'X',
               logoCardIssuer = state.creditCard.logoCardIssuer
           )
           Spacer(modifier = Modifier.size(16.dp))
           CreditCardForm(
               creditCard = state.creditCard,
               onFocusChanged = {
                   viewModel.onEvent(InstallmentEvent.OnFocusChanged(it))
               },
               onNumberValueChange = {
                   viewModel.onEvent(InstallmentEvent.OnNumberValueChange(it))
               },
               onNameValueChange = {
                   viewModel.onEvent(InstallmentEvent.OnNameValueChange(it))
               },
               onExpirationValueChange = {
                   viewModel.onEvent(InstallmentEvent.OnExpirationValueChange(it))
               },
               onCVCValueChange = {
                   viewModel.onEvent(InstallmentEvent.OnCVCValueChange(it))
               },
               onSetName = {
                   viewModel.onEvent(InstallmentEvent.OnNameValueChange(""))
               },
               onSetNumber= {
                   viewModel.onEvent(InstallmentEvent.OnNumberValueChange(""))
               },
               onSetExpiration = {
                   viewModel.onEvent(InstallmentEvent.OnExpirationValueChange(""))
               },
               onSetCVC= {
                   viewModel.onEvent(InstallmentEvent.OnCVCValueChange(""))
               },
               items = state.recommendedMessage,
               mSelectedText = state.selectedInstallment,
               mTextFieldSize = state.selectedInstallmentMessageSize,
               mExpanded = state.installmentExpanded,
               onValueChange = {
                   viewModel.onEvent(InstallmentEvent.OnValueChangeInstallment(it))
               },
               onClick = {
                   viewModel.onEvent(InstallmentEvent.OnInstallmentClick)
               },
               onSelect = {
                   viewModel.onEvent(InstallmentEvent.OnSelectInstallment(it))
               },
               onGloballyPositioned = {
                   viewModel.onEvent(InstallmentEvent.OnGloballyPositioned(it))
               },
               onDismissRequest = {
                   viewModel.onEvent(InstallmentEvent.OnDismissRequest)
               },
               onDropdownMenuFocusChanged = {
                   viewModel.onEvent(InstallmentEvent.OnDropdownMenuFocusChanged(it))
               }
           )
          }
        }
    }
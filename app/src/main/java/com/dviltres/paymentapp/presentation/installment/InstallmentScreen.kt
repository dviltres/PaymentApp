package com.dviltres.paymentapp.presentation.installment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.presentation.components.*
import com.dviltres.paymentapp.presentation.installment.components.HeaderInfo
import com.dviltres.paymentapp.presentation.installment.components.InstallmentItem
import com.dviltres.paymentapp.presentation.payment.PaymentEvent
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.presentation.util.navigation.Screen
import com.dviltres.paymentapp.presentation.util.navigation.StandardScaffold
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun InstallmentScreen(
    paymentMethodId:String,
    amount:Int,
    issuerId:String,
    navController: NavController,
    viewModel: InstallmentViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {

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
                       text = stringResource(R.string.payment_method),
                       isEnabled = true,
                       displayProgressBar = state.isLoading,
                       onClick = {

                       },
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(spacing.spaceMedium)
                   )
            }
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(InstallmentEvent.OnRefresh)
                },
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp
                    )
                ) {
                    CreditCard(
                        number = state.creditCard.number,
                        expiration = state.creditCard.expiration,
                        holderName = state.creditCard.holderName,
                        cvc = state.creditCard.cvc,
                        flipped = state.flipped,
                        emptyChar = 'X',
                        showSecurityCode = false
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    CreditCardForm(
                        flipped = state.flipped,
                        creditCard = state.creditCard,
                        onFocusChanged = {

                        },
                        onNumberValueChange = {

                        },
                        onNameValueChange = {

                        },
                        onExpirationValueChange = {

                        },
                        onCVCValueChange = {

                        },
                        onSetName = {

                        },
                        onSetNumber= {

                        },
                        onSetExpiration = {

                        },
                        onSetCVC= {

                        }
                    )
                }
                /*Column(modifier = Modifier.fillMaxSize()) {
                    Column( modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                        .padding(
                            start = spacing.spaceLarge,
                            top = spacing.spaceMedium,
                            end = spacing.spaceLarge,
                            bottom = spacing.spaceMedium,
                        )
                    ) {
                        if(state.paymentMethod != null && state.cardIssuer != null){
                            HeaderInfo(
                                paymentMethod = state.paymentMethod,
                                cardIssuer = state.cardIssuer
                            )
                        }
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = state.selectedInstallment,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                            OutlinedTextField(
                                value = state.token,
                                onValueChange = {
                                    // viewModel.onEvent(PaymentEvent.OnPaymentChange(it))
                                },
                                label = { Text(text = stringResource(id = R.string.security_code)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        *//*  focusManager.clearFocus()
                                          keyboardController?.hide()*//*
                                    }
                                ),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            text = stringResource(R.string.select_installments),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                    LazyColumn(
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .padding(bottom = 80.dp)
                    ){
                       items(state.recommendedMessage){ item->
                           InstallmentItem(
                               text = item,
                               onSelectedClick = {
                                   viewModel.onEvent(InstallmentEvent.OnSelectInstallment(item))
                               }
                           )
                       }
                    }
                }*/

            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.recommendedMessage.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.no_results),
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
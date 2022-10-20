package com.dviltres.paymentapp.presentation.payment

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.dviltres.paymentapp.domain.useCase.util.InputValidator
import com.dviltres.paymentapp.presentation.components.DefaultAppBar
import com.dviltres.paymentapp.presentation.components.RoundedButton
import com.dviltres.paymentapp.presentation.payment.components.PaymentItem
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.presentation.util.navigation.Screen
import com.dviltres.paymentapp.presentation.util.navigation.StandardScaffold
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val focusManager = LocalFocusManager.current
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )

    LaunchedEffect(key1 = state.expanded) {
        if (state.expanded) sheetState.show() else sheetState.hide()
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    navController.navigate(Screen.PaymentMethodScreen.route+"?amount=${event.data as String}")
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            Column {
                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_amount),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(
                                top = spacing.spaceMedium,
                                start = spacing.spaceMedium,
                                end = spacing.spaceMedium)
                    )
                    IconButton( onClick = {
                        viewModel.onEvent(PaymentEvent.OnCloseAddAmountView)
                        keyboardController?.hide()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(
                            id = R.string.backIcon))
                    }
                }
                Column( modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = spacing.spaceSmall,
                        start = spacing.spaceMedium,
                        end = spacing.spaceMedium,
                        bottom = spacing.spaceMedium)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.amount,
                        onValueChange = {
                            InputValidator.parseAmount(it)?.let { amount ->
                                viewModel.onEvent(PaymentEvent.OnAmountChange(amount))
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.amount)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                viewModel.onEvent(PaymentEvent.OnConfirmClick)
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    RoundedButton(
                        text = stringResource(R.string.payment_method),
                        isEnabled = state.amount.isNotBlank(),
                        displayProgressBar = state.isLoading,
                        onClick = {
                            viewModel.onEvent(PaymentEvent.OnConfirmClick)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp, topEnd =  20.dp
        )
    ) {
        StandardScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier,
            drawerGesturesEnabled = false,
            topBar = {
                DefaultAppBar(
                    title = stringResource(R.string.payment)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(PaymentEvent.OnAddAmountClick)
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_payment_icon),
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            bottomBar = {

            }
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(PaymentEvent.OnRefresh)
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spacing.spaceSmall),
                        state = scrollState
                    ) {
                        items(state.payments){ item->
                            PaymentItem(
                                item = item,
                                onSelectedClick = {

                                }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoading -> CircularProgressIndicator()
                        state.payments.isEmpty() -> {
                            Text(
                                text = stringResource(R.string.no_payment),
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.dviltres.paymentapp.presentation.payment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.dviltres.paymentapp.data.common.Constants
import com.dviltres.paymentapp.presentation.components.CustomAppBar
import com.dviltres.paymentapp.presentation.components.RoundedButton
import com.dviltres.paymentapp.presentation.components.SearchState
import com.dviltres.paymentapp.presentation.payment.components.PaymentItem
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.presentation.theme.Red
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
        isRefreshing = viewModel.state.isRefreshing
    )

    LaunchedEffect(key1 = state.expanded) {
        if (state.expanded) sheetState.show() else sheetState.hide()
    }

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
                        viewModel.onEvent(PaymentEvent.OnCloseAddPaymentView)
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
                            viewModel.onEvent(PaymentEvent.OnPaymentChange(it))
                        },
                        label = { Text(text = stringResource(id = R.string.amount)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    RoundedButton(
                        text = stringResource(R.string.payment_method),
                        isEnabled = state.amount.isNotBlank(),
                        displayProgressBar = state.isLoading,
                        onClick = {
                            navController.navigate(Screen.PaymentMethodScreen.route+"?amount=${state.amount.toInt()}")
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
                CustomAppBar(
                    title = stringResource(R.string.payments),
                    searchState = state.searchState,
                    searchTextState = state.query,
                    onTextChange = {
                        viewModel.onEvent(PaymentEvent.OnUpdateSearchText(it))
                    },
                    onCloseClicked = {
                        viewModel.onEvent(PaymentEvent.OnUpdateSearchState(SearchState.CLOSED))
                    },
                    onSearchClicked = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(PaymentEvent.OnUpdateSearchState(SearchState.OPENED))
                            }){
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = stringResource(R.string.search_icon),
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(PaymentEvent.OnAddPaymentClick)
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
                            .padding(top = spacing.spaceMedium)
                            .background(MaterialTheme.colors.background),
                        contentPadding = PaddingValues(top = 100.dp),
                        state = scrollState
                    ) {
                        itemsIndexed(
                            items = state.payments
                        ) { index, item ->
                            viewModel.onEvent(PaymentEvent.OnChangeScrollPosition(index))
                            if ((index + 1) >= (state.page * Constants.PAGE_SIZE) && !state.isLoading) {
                                viewModel.onEvent(PaymentEvent.OnTriggerNextPage)
                            }
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
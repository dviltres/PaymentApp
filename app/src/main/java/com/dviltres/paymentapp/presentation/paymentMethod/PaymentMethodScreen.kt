package com.dviltres.paymentapp.presentation.paymentMethod

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.presentation.components.BottomBarButton
import com.dviltres.paymentapp.presentation.components.CustomAppBar
import com.dviltres.paymentapp.presentation.components.SearchState
import com.dviltres.paymentapp.presentation.paymentMethod.components.PaymentMethodItem
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.presentation.util.navigation.Screen
import com.dviltres.paymentapp.presentation.util.navigation.StandardScaffold
import com.dviltres.paymentapp.presentation.util.uiEvent.UiEvent
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentMethodScreen(
    amount:Int,
    navController: NavController,
    viewModel: PaymentMethodViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val spacing = LocalSpacing.current
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    LaunchedEffect(key1 = state.expanded) {
        if (state.expanded)
            sheetState.show()
        else if(state.selectedPaymentMethod != null)
            sheetState.show()
        else
             sheetState.hide()
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
            Text(
                text = stringResource(id = R.string.amount_to_pay, amount.toString()),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(top = spacing.spaceMedium)
            )
            if(state.selectedPaymentMethod != null){
                BottomBarButton(
                    text = state.selectedPaymentMethod.name,
                    icon =  state.selectedPaymentMethod.thumbnail,
                    onClick = {
                        navController.navigate(Screen.CardIssuerScreen.route +"?payment_method_id=${state.selectedPaymentMethod.id}&amount=${amount}")
                    }
                )
            }
            else {
                BottomBarButton(
                    text = stringResource(id = R.string.select_payment_method),
                    icon = null,
                    onClick = {
                        viewModel.onEvent(PaymentMethodEvent.OnShowErrorMessage(UiText.StringResource(R.string.no_selected_payment_method).asString(context)))
                    }
                )
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.primary,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp, topEnd =  20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
        )
    ){
        StandardScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier,
            drawerGesturesEnabled = false,
            topBar = {
                CustomAppBar(
                    title = stringResource(R.string.payment_method),
                    searchState = state.searchState,
                    searchTextState = state.query,
                    onTextChange = {
                        viewModel.onEvent(PaymentMethodEvent.OnUpdateSearchText(it))
                    },
                    onCloseClicked = {
                        viewModel.onEvent(PaymentMethodEvent.OnUpdateSearchState(SearchState.CLOSED))
                    },
                    onSearchClicked = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(Screen.PaymentScreen.route)
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(
                                id = R.string.backIcon))
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(PaymentMethodEvent.OnUpdateSearchState(SearchState.OPENED))
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
            bottomBar = {

            }
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(PaymentMethodEvent.OnRefresh)
                },
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                ){
                    items(state.paymentMethods){ item->
                        PaymentMethodItem(
                            item = item,
                            background = MaterialTheme.colors.background,
                            onSelectedClick = {
                                viewModel.onEvent(PaymentMethodEvent.OnSelectPaymentMethod(item))
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
                    state.paymentMethods.isEmpty() -> {
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
}
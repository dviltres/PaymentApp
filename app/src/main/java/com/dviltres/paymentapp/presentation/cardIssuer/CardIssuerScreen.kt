package com.dviltres.paymentapp.presentation.cardIssuer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.presentation.cardIssuer.components.CardIssuerItem
import com.dviltres.paymentapp.presentation.components.BottomBarButton
import com.dviltres.paymentapp.presentation.components.CustomAppBar
import com.dviltres.paymentapp.presentation.components.DefaultAppBar
import com.dviltres.paymentapp.presentation.components.SearchState
import com.dviltres.paymentapp.presentation.paymentMethod.PaymentMethodEvent
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
fun CardIssuerScreen(
    paymentMethodId:String,
    amount:Int,
    navController: NavController,
    viewModel: CardIssuerViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    LaunchedEffect(key1 = state.expanded) {
        if (state.expanded)
            sheetState.show()
        else if(state.selectedCardIssuer != null)
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
            if(state.selectedCardIssuer != null){
                BottomBarButton(
                    text = state.selectedCardIssuer.name,
                    icon =  state.selectedCardIssuer.thumbnail,
                    onClick = {
                        navController.navigate(Screen.InstallmentScreen.route +"?payment_method_id=${paymentMethodId}&amount=${amount}&issuer_id=${state.selectedCardIssuer.id}")
                    }
                )
            }
            else {
                BottomBarButton(
                    text = stringResource(id = R.string.select_card_issuer),
                    icon = null,
                    onClick = {
                        viewModel.onEvent(CardIssuerEvent.OnShowErrorMessage(UiText.StringResource(R.string.no_selected_card_issuer).asString(context)))
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
                DefaultAppBar(
                    title = stringResource(R.string.card_issuer),
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(Screen.PaymentMethodScreen.route+"?amount=${amount}")
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(
                                id = R.string.backIcon))
                        }
                    },
                )
            },
            bottomBar = {

            }
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(CardIssuerEvent.OnRefresh)
                },
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Column( modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                        .padding(
                            start = spacing.spaceLarge,
                            top = spacing.spaceMedium,
                            end = spacing.spaceLarge,
                            bottom = spacing.spaceMedium,
                        )
                    ) {

                       state.paymentMethod?.let {
                           PaymentMethodItem(
                               item = it,
                               background = MaterialTheme.colors.primary,
                               onSelectedClick = {

                               }
                           )
                       }
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        Text(
                            text = stringResource(R.string.available_card_issuer),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                    ){
                        items(state.cardIssuers){ item->
                            CardIssuerItem(
                                item = item,
                                onSelectedClick = {
                                    viewModel.onEvent(CardIssuerEvent.OnSelectPaymentMethod(item))
                                }
                            )
                        }
                    }
                }

            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.cardIssuers.isEmpty() -> {
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
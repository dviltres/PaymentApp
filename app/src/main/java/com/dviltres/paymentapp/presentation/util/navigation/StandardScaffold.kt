package com.dviltres.paymentapp.presentation.util.navigation

import android.annotation.SuppressLint
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StandardScaffold(
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar : @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerGesturesEnabled:Boolean=true,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition ,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
        topBar = {
           topBar()
         },
        bottomBar = {
           bottomBar()
        },
        drawerGesturesEnabled = drawerGesturesEnabled,
        modifier = modifier

    ) {
        content()
    }
}
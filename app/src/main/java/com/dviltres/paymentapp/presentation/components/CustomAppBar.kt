package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

@Composable
fun CustomAppBar(
    title:String,
    searchState: SearchState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
    ) {
    when (searchState) {
        SearchState.CLOSED -> {
            DefaultAppBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions
            )
        }
        SearchState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}
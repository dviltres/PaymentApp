package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.toSize
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.domain.useCase.util.InputValidator

@Composable
fun DropdownMenu(
    items:List<String>,
    mSelectedText:String,
    mTextFieldSize:Size,
    mExpanded:Boolean,
    focusRequester:FocusRequester,
    onValueChange:(String) -> Unit,
    onClick:() -> Unit,
    onSelect:(String) -> Unit,
    onGloballyPositioned:(Size) -> Unit,
    onDismissRequest:() -> Unit,
    onFocusChanged:(Boolean)-> Unit,
) {
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    onFocusChanged(state.isFocused)
                 }
                .focusRequester(focusRequester)
                .onGloballyPositioned { coordinates ->
                    onGloballyPositioned(coordinates.size.toSize())
                },
            value = mSelectedText,
            readOnly = true,
            label = stringResource(id = R.string.installments),
            onValueChange = {
                InputValidator.parseHolderName(it)?.let { name ->
                    onValueChange(name)
                }
            },
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable {
                        onClick()
                    })
            }
        )
        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(with(LocalDensity.current){ mTextFieldSize.width.toDp()})
        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    onSelect(label)
                    onDismissRequest()
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

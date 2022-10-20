package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.domain.useCase.util.InputValidator

@Composable
fun CreditCardForm(
    creditCard: CreditCard,
    onFocusChanged:(Boolean) -> Unit,
    onNumberValueChange:(String) -> Unit,
    onNameValueChange:(String) -> Unit,
    onExpirationValueChange:(String) -> Unit,
    onCVCValueChange:(String) -> Unit,
    onSetName:() -> Unit,
    onSetNumber:() -> Unit,
    onSetExpiration:() -> Unit,
    onSetCVC:() -> Unit,
    items:List<String>,
    mSelectedText:String,
    mTextFieldSize: Size,
    mExpanded:Boolean,
    onValueChange:(String) -> Unit,
    onClick:() -> Unit,
    onSelect:(String) -> Unit,
    onGloballyPositioned:(Size) -> Unit,
    onDismissRequest:() -> Unit,
    onDropdownMenuFocusChanged:(Boolean) -> Unit,
) {
    val focusHolderName = FocusRequester()
    val focusExpiration = FocusRequester()
    val focusCVC = FocusRequester()
    val focusInstallment = FocusRequester()

    Column {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    onFocusChanged(state.isFocused)
                },
            value = creditCard.number,
            label = stringResource(id = R.string.number_card),
            onValueChange = {
                InputValidator.parseNumber(it)?.let { number ->
                    onNumberValueChange(number)
                    if (creditCard.number.length >= 16) focusHolderName.requestFocus()
                }
            },
            trailingIcon = {
                CustomTextFieldDeleteIcon(value = creditCard.number) {
                    onSetNumber()
                }
            },
            keyboardType = KeyboardType.Number,
            nextFocus = focusHolderName
        )
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    onFocusChanged(state.isFocused)
                }
                .focusRequester(focusHolderName),
            value = creditCard.holderName,
            label = stringResource(id = R.string.holder_name),
            onValueChange = {
                InputValidator.parseHolderName(it)?.let { name ->
                    onNameValueChange(name)
                }
            },
            trailingIcon = {
                CustomTextFieldDeleteIcon(value = creditCard.holderName) {
                    onSetName()
                }
            },
            nextFocus = focusExpiration
        )

        Row {
            CustomTextField(
                modifier = Modifier
                    .weight(0.5f)
                    .onFocusEvent { state ->
                        onFocusChanged(state.isFocused)
                    }
                    .focusRequester(focusExpiration),
                value = creditCard.expiration,
                label = stringResource(id = R.string.expiration),
                onValueChange = { expiration->
                    onExpirationValueChange(expiration)
                    if (creditCard.expiration.length >= 4) focusCVC.requestFocus()
                },
                trailingIcon = {
                    CustomTextFieldDeleteIcon(value = creditCard.expiration) {
                        onSetExpiration()
                    }
                },
                keyboardType = KeyboardType.Number,
                nextFocus = focusCVC
            )

            CustomTextField(
                modifier = Modifier
                    .weight(0.5f)
                    .onFocusEvent { state ->
                        onFocusChanged(state.isFocused)
                    }
                    .focusRequester(focusCVC),
                value = creditCard.cvc,
                label = stringResource(id = R.string.cvc),
                onValueChange = {
                    InputValidator.parseCVC(it)?.let { cvc ->
                        onCVCValueChange(cvc)
                        if (cvc.length >= 3) focusInstallment.requestFocus()
                    }
                },
                trailingIcon = {
                    CustomTextFieldDeleteIcon(value = creditCard.cvc) {
                        onSetCVC()
                    }
                },
                keyboardType = KeyboardType.Number,
                nextFocus = focusInstallment
            )
        }
        DropdownMenu(
            items = items,
            mSelectedText = mSelectedText,
            mTextFieldSize = mTextFieldSize,
            mExpanded = mExpanded,
            onValueChange = onValueChange,
            onClick = onClick,
            onSelect = onSelect,
            onGloballyPositioned = onGloballyPositioned,
            onDismissRequest = onDismissRequest,
            onFocusChanged = onDropdownMenuFocusChanged,
            focusRequester = focusInstallment,
        )
    }
}
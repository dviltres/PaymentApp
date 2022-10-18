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
import androidx.compose.ui.text.input.KeyboardType
import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.presentation.util.FieldType
import com.dviltres.paymentapp.presentation.util.InputValidator

@Composable
fun CreditCardForm(
    flipped:Boolean,
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
) {
    val focusHolderName = FocusRequester()
    val focusExpiration = FocusRequester()
    val focusCVC = FocusRequester()

    Column {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    //if (state.isFocused) flipped = false
                    onFocusChanged(state.isFocused)
                },
            value = creditCard.number,
            label = "Number of card",
            // visualTransformation = InputTransformation(FieldType.CARD_NUMBER),
            onValueChange = {
                onNumberValueChange(it)
                   /* if (number.length >= 16) number.substring(0..15) else it*/

                // When value is completed, request focus of next field
                if (creditCard.number.length >= 16) focusHolderName.requestFocus()
            },
            trailingIcon = {
                CustomTextFieldDeleteIcon(value = creditCard.number) {
                    //viewModel.number = ""
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
                    //if (state.isFocused) viewModel.flipped = false
                    onFocusChanged(state.isFocused)
                }
                .focusRequester(focusHolderName),
            value = creditCard.holderName,
            label = "Name of card",
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
                       /* if (state.isFocused) viewModel.flipped = false*/
                    }
                    .focusRequester(focusExpiration),
                value = creditCard.expiration,
                label = "Expiration",
             //   visualTransformation = InputTransformation(FieldType.EXPIRATION),
                onValueChange = { expiration->
                   // viewModel.expiration = if (it.length >= 4) it.substring(0..3) else it

                    onExpirationValueChange(expiration)

                    // When value is completed, request focus of next field
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
                label = "CVC",
                onValueChange = {
                    InputValidator.parseCVC(it)?.let { cvc ->
                        onCVCValueChange(cvc)
                    }
                },
                trailingIcon = {
                    CustomTextFieldDeleteIcon(value = creditCard.cvc) {
                        onSetCVC()
                    }
                },
                keyboardType = KeyboardType.Number
            )
        }
    }
}
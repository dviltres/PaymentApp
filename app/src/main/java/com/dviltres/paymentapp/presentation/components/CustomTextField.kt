package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dviltres.paymentapp.R

@Composable
fun CustomTextFieldDeleteIcon(
    value: String,
    onClick: () -> Unit
) {
    if (value.isNotBlank()) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { onClick() }
                .testTag("deleteIconContainer")
        ) {
            Icon(
                modifier = Modifier
                    .padding(5.dp)
                    .testTag("deleteIcon"),
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    nextFocus: FocusRequester? = null,
    label: String,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val keyboardOptions = KeyboardOptions(
        keyboardType = keyboardType,
        imeAction = if (nextFocus != null) ImeAction.Next else ImeAction.Done
    )

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        visualTransformation = visualTransformation,
        modifier = modifier,
        readOnly = readOnly,
        label = { Text(text = label) },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = {
                nextFocus?.requestFocus()
            }
        )
    )
}
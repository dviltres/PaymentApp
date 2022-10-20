package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dviltres.paymentapp.presentation.theme.LocalSpacing

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    isEnabled:Boolean = true,
    text: String,
    displayProgressBar: Boolean = false,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    Box( modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
        )  {
        if (!displayProgressBar) {
            Button(
                modifier = modifier.width(300.dp).height(50.dp),
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(spacing.spaceMedium),
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White
                    )
                )
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                color = MaterialTheme.colors.primary,
                strokeWidth = 6.dp
            )
        }
    }
}
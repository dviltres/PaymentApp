package com.dviltres.paymentapp.presentation.installment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dviltres.paymentapp.presentation.theme.LocalSpacing

@Composable
fun InstallmentItem (
    text: String,
    onSelectedClick: () -> Unit,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
    borderShape: Shape = RoundedCornerShape(size = 10.dp),
    modifier: Modifier = Modifier
){

    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(
                spacing.spaceTen,
                spacing.spaceFive,
                spacing.spaceTen,
                spacing.spaceFive
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = borderShape
            )
            .clickable {
                onSelectedClick()
            },
        elevation = spacing.spaceTen,
        shape = RoundedCornerShape(spacing.spaceTen)
    )
    {
        Row (modifier = Modifier.padding(spacing.spaceSmall)) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}
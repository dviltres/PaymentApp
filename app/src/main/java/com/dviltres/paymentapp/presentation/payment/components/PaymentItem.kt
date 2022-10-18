package com.dviltres.paymentapp.presentation.payment.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PaymentItem (
    item: Payment,
    onSelectedClick: () -> Unit,
    modifier: Modifier = Modifier
){

    val spacing = LocalSpacing.current

    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(spacing.spaceSmall)
            .clickable { onSelectedClick() }
            .fillMaxWidth(),
        elevation = spacing.spaceSmall,
    )
    {
        Column(
            modifier = modifier.padding(start = spacing.spaceMedium,
                bottom = spacing.spaceMedium,
                top = spacing.spaceMedium,
                end = spacing.spaceSmall
            ),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            val painter =  rememberImagePainter(
                data = item.paymentMethod.thumbnail,
                builder = {
                    placeholder(R.drawable.ic_background_image)
                    error(R.drawable.ic_broken_imagen)
                    crossfade(1000)
                }
            )
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.payment_method_image),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = item.paymentMethod.name,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = Color.White,
            )
        }

    }
}
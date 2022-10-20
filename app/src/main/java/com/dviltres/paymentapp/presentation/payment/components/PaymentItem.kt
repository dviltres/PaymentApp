package com.dviltres.paymentapp.presentation.payment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.data.common.Constants
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.useCase.util.toString
import com.dviltres.paymentapp.presentation.theme.Gray
import com.dviltres.paymentapp.presentation.theme.LocalSpacing

@Composable
fun PaymentItem (
    item: Payment,
    onSelectedClick: () -> Unit,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
    borderShape: Shape = RoundedCornerShape(size = 10.dp),
    modifier: Modifier=Modifier
){
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectedClick() }
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
            ),
        elevation = spacing.spaceTen,
        shape = RoundedCornerShape(spacing.spaceTen)
    )
    {
        Column(
            modifier = modifier
                .background(Gray)
                .padding(spacing.spaceMedium)
        ) {
            Row( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(item.cardIssuer!!.thumbnail)
                            .crossfade(true)
                            .size(50)
                            .build(),
                        placeholder =  painterResource(R.drawable.ic_background_image),
                        error = painterResource(R.drawable.ic_broken_imagen),
                        contentDescription = stringResource(R.string.payment_method_image),
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                    Text(
                        text = item.cardIssuer!!.name,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Column {
                    Text(
                        text = item.date!!.toString(Constants.DATE_FORMAT),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.paymentMethod!!.thumbnail)
                    .crossfade(true)
                    .size(80)
                    .build(),
                placeholder =  painterResource(R.drawable.ic_background_image),
                error = painterResource(R.drawable.ic_broken_imagen),
                contentDescription = stringResource(R.string.payment_method_image),
                contentScale = ContentScale.None,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = item.paymentMethod!!.name,
                style = MaterialTheme.typography.button,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row {
                    Text(
                        text = stringResource(id = R.string.amount_header),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                    Text(
                        text = item.amount.toString(),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = item.installment!!,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                )
            }

        }

    }


}
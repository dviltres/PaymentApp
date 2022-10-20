package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.presentation.theme.LocalSpacing
import com.dviltres.paymentapp.domain.useCase.util.CardNumberParser
import com.dviltres.paymentapp.presentation.util.ext.dynamicCardHeight


@Composable
private fun CreditCardContainer(
    backgroundColor: Color = MaterialTheme.colors.primary,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .dynamicCardHeight(),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.credit_card_round_corner)),
        backgroundColor = backgroundColor
    ) {
        content()
    }
}

@Composable
private fun CardNumberBlock(cardNumber: CardNumberParser, modifier: Modifier) {
    Text(
        modifier = modifier,
        fontWeight = FontWeight.Light,
        fontFamily = FontFamily.Monospace,
        fontSize = 25.sp,
        color = Color.White,
        text = "${cardNumber.first} ${cardNumber.second} ${cardNumber.third} ${cardNumber.fourth}"
    )
}

@Composable
fun CreditCard(
    number: String,
    expiration: String,
    holderName: String,
    cvc: String,
    logoCardIssuer:String,
    emptyChar: Char = 'x',
    backgroundColor: Color = MaterialTheme.colors.primary
) {
    val spacing = LocalSpacing.current
    val model = CreditCard(
        number = number,
        expiration = expiration,
        holderName = holderName,
        cvc = cvc,
        logoCardIssuer = logoCardIssuer
    )
    CreditCardContainer(
        backgroundColor = backgroundColor
    ) {
        ConstraintLayout {
            val (
                iChip,
                lCardNumber,
                lCVC,
                lCVCNumber,
                lExpiration,
                lExpirationDate,
                lHolderName,
                iCardEntity
            ) = createRefs()

            val cardNumber = CardNumberParser(
                number = model.number,
                emptyChar = emptyChar
            )

            val cardPadding = dimensionResource(R.dimen.credit_card_padding)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.logoCardIssuer)
                    .crossfade(true)
                    .size(50)
                    .build(),
                placeholder =  painterResource(R.drawable.ic_background_image),
                error = painterResource(R.drawable.ic_broken_imagen),
                contentDescription = stringResource(R.string.payment_method_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(iCardEntity) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(spacing.spaceMedium)
                    .width(40.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.chip_credit_card),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(iChip) {
                        top.linkTo(parent.top, margin = 50.dp)
                        start.linkTo(parent.start, margin = cardPadding)
                    }
                    .width(50.dp)
            )

            CardNumberBlock(
                modifier = Modifier
                    .constrainAs(lCardNumber) {
                        top.linkTo(iChip.bottom, margin = spacing.spaceTwo)
                        start.linkTo(iChip.start)
                    },
                cardNumber = cardNumber
            )

            Text(
                modifier = Modifier
                    .constrainAs(lCVC) {
                        top.linkTo(lCardNumber.bottom, margin = spacing.spaceSmall)
                        start.linkTo(lCardNumber.start)
                        centerHorizontallyTo(parent)
                    },
                fontSize = 12.sp,
                color = Color.White,
                text = stringResource(id = R.string.cvc)
            )

            Text(
                modifier = Modifier
                    .constrainAs(lCVCNumber) {
                        start.linkTo(lCVC.end, margin = spacing.spaceTen)
                        centerVerticallyTo(lCVC)
                    },
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color.White,
                text = model.cvc
            )

            Text(
                modifier = Modifier
                    .constrainAs(lHolderName) {
                        start.linkTo(parent.start, margin = cardPadding)
                        bottom.linkTo(parent.bottom, margin = 30.dp)
                    },
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color.White,
                text = if(model.holderName.isEmpty())
                    stringResource(id = R.string.your_name)
                else
                    model.holderName.uppercase()
            )

            Text(
                modifier = Modifier
                    .constrainAs(lExpiration) {
                        end.linkTo(parent.end, margin = 60.dp)
                        centerVerticallyTo(lHolderName)
                    },
                fontSize = 12.sp,
                color = Color.White,
                text = stringResource(id = R.string.exp)
            )

            Text(
                modifier = Modifier
                    .constrainAs(lExpirationDate) {
                        start.linkTo(lExpiration.end, margin = spacing.spaceTen)
                        end.linkTo(parent.end, margin = cardPadding)
                        centerVerticallyTo(lExpiration)
                    },
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color.White,
                text = model.formattedExpiration.ifEmpty { "00/00" }
            )
        }
    }
}
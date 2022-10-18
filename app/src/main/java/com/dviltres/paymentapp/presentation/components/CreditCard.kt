package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dviltres.paymentapp.presentation.util.CardNumberParser
import com.dviltres.paymentapp.presentation.util.ext.dynamicCardHeight
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.domain.model.CreditCard


@Composable
private fun CreditCardContainer(
    backgroundColor: Color = Color.Blue,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .dynamicCardHeight()
            .testTag("creditCardContainer"),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.credit_card_round_corner)),
        backgroundColor = backgroundColor
    ) {
        content()
    }
}

@Composable
private fun CreditCardFrontSide(
    model: CreditCard,
    emptyChar: Char = 'x',
    backgroundColor: Color = Color.Blue
) {
    CreditCardContainer(
        backgroundColor = backgroundColor
    ) {
        ConstraintLayout {
            val (
                iChip,
                lCardNumber,
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
                    .size(80)
                    .build(),
                placeholder =  painterResource(R.drawable.ic_background_image),
                error = painterResource(R.drawable.ic_broken_imagen),
                contentDescription = stringResource(R.string.payment_method_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(iCardEntity) {
                        top.linkTo(parent.top, margin = cardPadding)
                        end.linkTo(parent.end, margin = cardPadding)
                    }
                    .width(60.dp)
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
                        top.linkTo(iChip.bottom, margin = 2.dp)
                        start.linkTo(iChip.start)
                    }
                    .testTag("lCardNumber"),
                cardNumber = cardNumber
            )

            Text(
                modifier = Modifier
                    .constrainAs(lHolderName) {
                        start.linkTo(parent.start, margin = cardPadding)
                        bottom.linkTo(parent.bottom, margin = 30.dp)
                    }
                    .testTag("lHolderName"),
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color.White,
                text = if(model.holderName.isEmpty()) "YOUR NAME" else model.holderName.uppercase()
            )

            Text(
                modifier = Modifier
                    .constrainAs(lExpiration) {
                        end.linkTo(parent.end, margin = 60.dp)
                        centerVerticallyTo(lHolderName)
                    },
                fontSize = 12.sp,
                color = Color.White,
                text = "EXP"
            )

            Text(
                modifier = Modifier
                    .constrainAs(lExpirationDate) {
                        start.linkTo(lExpiration.end, margin = 10.dp)
                        end.linkTo(parent.end, margin = cardPadding)
                        centerVerticallyTo(lExpiration)
                    }
                    .testTag("lExpirationDate"),
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color.White,
                text = model.formattedExpiration.ifEmpty { "00/00" }
            )
        }
    }
}

@Composable
private fun CreditCardBackSide(
    model: CreditCard,
    showSecurityCode: Boolean = false,
    emptyChar: Char = 'x',
    backgroundColor: Color = Color.Blue
) {
    CreditCardContainer(
        backgroundColor = backgroundColor
    ) {
        ConstraintLayout {
            val (magneticStrip, signature, cvc, cardEntity) = createRefs()
            val cardPadding = dimensionResource(id = R.dimen.credit_card_padding)
            val cardNumber = CardNumberParser(
                number = model.number,
                emptyChar = emptyChar
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.credit_card_magnetic_band_height))
                    .background(Color.Black)
                    .constrainAs(magneticStrip) {
                        top.linkTo(parent.top, margin = cardPadding)
                    }
            )

            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(30.dp)
                    .background(Color.White)
                    .constrainAs(signature) {
                        top.linkTo(magneticStrip.bottom, margin = cardPadding)
                        start.linkTo(parent.start, margin = cardPadding)
                    }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(end = 5.dp)
                        .testTag("cardNumberSignature"),
                    textAlign = TextAlign.End,
                    text = cardNumber.fourth
                )
            }

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(30.dp)
                    .background(Color.White)
                    .constrainAs(cvc) {
                        top.linkTo(signature.top)
                        start.linkTo(signature.end, margin = cardPadding)
                    }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .testTag("cvc"),
                    textAlign = TextAlign.Center,
                    text = if (showSecurityCode) model.cvc else "*".repeat(model.cvc.length)
                )
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.logoCardIssuer)
                    .crossfade(true)
                    .size(80)
                    .build(),
                placeholder =  painterResource(R.drawable.ic_background_image),
                error = painterResource(R.drawable.ic_broken_imagen),
                contentDescription = stringResource(R.string.payment_method_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(cardEntity) {
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
                    .width(60.dp)
            )
        }
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
    emptyChar: Char = 'x',
    backgroundColor: Color = Color.Blue,
    showSecurityCode: Boolean = false,
    flipped: Boolean = false
) {
    val model = CreditCard(
        number = number,
        expiration = expiration,
        holderName = holderName,
        cvc = cvc
    )
    if (flipped) {
        CreditCardBackSide(
            model = model,
            emptyChar = emptyChar,
            backgroundColor = backgroundColor,
            showSecurityCode = showSecurityCode
        )
    } else {
        CreditCardFrontSide(
            model = model,
            emptyChar = emptyChar,
            backgroundColor = backgroundColor
        )
    }
}
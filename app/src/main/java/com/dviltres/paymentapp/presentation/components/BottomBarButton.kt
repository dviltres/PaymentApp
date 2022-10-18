package com.dviltres.paymentapp.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.presentation.theme.LocalSpacing

@Composable
fun BottomBarButton(
    text:String,
    icon:String? = null,
    onClick:()->Unit
) {
    val spacing = LocalSpacing.current
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        elevation = spacing.spaceSmall,
    )  {
        Button(
                modifier = Modifier.width(300.dp).height(50.dp),
                onClick = onClick
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                icon?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .crossfade(true)
                            .size(60)
                            .build(),
                        placeholder =  painterResource(R.drawable.ic_background_image),
                        error = painterResource(R.drawable.ic_broken_imagen),
                        contentDescription = stringResource(R.string.bottom_bar_icon),
                        contentScale = ContentScale.None
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceMedium))
                    Icon(
                        Icons.Filled.ArrowForward, contentDescription = stringResource(
                            id = R.string.nextIcon))
                }
         }
    }
}
package com.dviltres.paymentapp.data.mapper

import com.dviltres.paymentapp.data.local.entity.CardIssuerEntity
import com.dviltres.paymentapp.data.remote.dto.cardIssuer.CardIssuerDto
import com.dviltres.paymentapp.domain.model.CardIssuer


fun CardIssuerEntity.toCardIssuer(): CardIssuer {
    return CardIssuer(
         id = id,
         name = name,
         secure_thumbnail = secure_thumbnail,
         thumbnail = thumbnail
    )
}

fun CardIssuerDto.toCardIssuerEntity(): CardIssuerEntity {
    return CardIssuerEntity(
        id = id,
        name = name,
        secure_thumbnail = secure_thumbnail,
        thumbnail = thumbnail
    )
}
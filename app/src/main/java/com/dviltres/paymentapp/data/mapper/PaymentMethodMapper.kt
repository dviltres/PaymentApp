package com.dviltres.paymentapp.data.mapper

import com.dviltres.paymentapp.data.local.entity.PaymentMethodEntity
import com.dviltres.paymentapp.data.remote.dto.paymentMethod.PaymentMethodDto
import com.dviltres.paymentapp.domain.model.PaymentMethod

fun PaymentMethodEntity.toPaymentMethod(): PaymentMethod {
    return PaymentMethod(
        id = id,
        name = name,
        secure_thumbnail = secure_thumbnail,
        thumbnail = thumbnail
    )
}

fun PaymentMethodDto.toPaymentMethodEntity(): PaymentMethodEntity {
    return PaymentMethodEntity(
        id = id,
        name = name,
        secure_thumbnail = secure_thumbnail,
        thumbnail = thumbnail
    )
}
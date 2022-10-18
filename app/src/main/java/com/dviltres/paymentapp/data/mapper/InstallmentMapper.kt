package com.dviltres.paymentapp.data.mapper

import com.dviltres.paymentapp.data.local.entity.InstallmentEntity
import com.dviltres.paymentapp.data.remote.dto.installment.InstallmentDto
import com.dviltres.paymentapp.domain.model.Installment

fun InstallmentEntity.toInstallment(): Installment {
    return Installment(
         issuerId = issuerId,
         payment_method_id = payment_method_id,
         recommended_message = recommended_message,
    )
}

fun InstallmentDto.toInstallmentEntity(): InstallmentEntity {
    return InstallmentEntity(
        issuerId = issuer.id,
        payment_method_id = payment_method_id,
        recommended_message = payer_costs.map {
            it.recommended_message
        }
    )
}
package com.dviltres.paymentapp.data.remote.dto.paymentMethod

data class Setting(
    val bin: Bin,
    val card_number: CardNumber,
    val security_code: SecurityCode
)
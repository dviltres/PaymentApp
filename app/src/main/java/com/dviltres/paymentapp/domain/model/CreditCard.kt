package com.dviltres.paymentapp.domain.model


data class CreditCard(
    var number: String = "",
    var expiration: String = "0000", // First two digits = month, last two digits = year
    var holderName: String = "",
    var cvc: String = "000",
    var cardEntity: String = "VISA",
    var logoCardIssuer:String = ""
) {
    /** Concat an slash on the middle of the string following the format mm/yy */
    val formattedExpiration = when {
        expiration.length == 2 -> "$expiration/"
        expiration.length > 2 -> expiration.substring(0, 2) + "/" + expiration.substring(2, expiration.length)
        else -> expiration
    }
}
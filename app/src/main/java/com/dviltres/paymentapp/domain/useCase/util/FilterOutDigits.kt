package com.dviltres.paymentapp.domain.useCase.util

class FilterOutDigits {
    operator fun invoke(text: String): String {
        return text.filter { it.isDigit() }
    }
}
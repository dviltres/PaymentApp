package com.dviltres.paymentapp.presentation.util.navigation

sealed class Screen(val route: String){
    object PaymentScreen: Screen(route = "payment_screen")
    object PaymentMethodScreen: Screen(route = "payment_method_screen")
    object CardIssuerScreen: Screen(route = "card_issuer_screen")
    object InstallmentScreen: Screen(route = "installment_screen")
}

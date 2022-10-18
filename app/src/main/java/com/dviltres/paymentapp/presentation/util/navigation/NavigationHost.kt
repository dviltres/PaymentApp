package com.dviltres.paymentapp.presentation.util.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dviltres.paymentapp.presentation.cardIssuer.CardIssuerScreen
import com.dviltres.paymentapp.presentation.installment.InstallmentScreen
import com.dviltres.paymentapp.presentation.payment.PaymentScreen
import com.dviltres.paymentapp.presentation.paymentMethod.PaymentMethodScreen
import com.dviltres.paymentapp.presentation.util.navigation.Screen

@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: String,
)  {
    NavHost(
        navController = navController,
        startDestination = startDestination
      ) {
        composable(Screen.PaymentScreen.route) {
            PaymentScreen(navController = navController)
        }
        composable(Screen.PaymentMethodScreen.route+"?amount={amount}",
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            BackHandler(true) {
                navController.navigate(Screen.PaymentScreen.route)
            }
            val amount = it.arguments?.getInt("amount",0)?:0
            PaymentMethodScreen(
                navController = navController,
                amount = amount
            )
        }
        composable(Screen.CardIssuerScreen.route+"?payment_method_id={payment_method_id}&amount={amount}",
            arguments = listOf(
                navArgument("payment_method_id") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("amount") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            BackHandler(true) {

            }
            val paymentMethodId = it.arguments?.getString("payment_method_id","")?:""
            val amount = it.arguments?.getInt("amount",0)?:0
            CardIssuerScreen(
                navController = navController,
                paymentMethodId = paymentMethodId,
                amount = amount
            )
        }
        composable(Screen.InstallmentScreen.route+"?payment_method_id={payment_method_id}&amount={amount}&issuer_id={issuer_id}",
            arguments = listOf(
                navArgument("payment_method_id") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("amount") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("issuer_id") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            BackHandler(true) {

            }
            val paymentMethodId = it.arguments?.getString("payment_method_id","")?:""
            val amount = it.arguments?.getInt("amount",0)?:0
            val issuerId =  it.arguments?.getString("issuer_id","")?:""
            InstallmentScreen(
                navController = navController,
                paymentMethodId = paymentMethodId,
                amount = amount,
                issuerId = issuerId
            )
        }
     }

}
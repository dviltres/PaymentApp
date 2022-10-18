package com.dviltres.paymentapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dviltres.paymentapp.presentation.theme.PaymentAppTheme
import com.dviltres.paymentapp.presentation.util.navigation.NavigationHost
import com.dviltres.paymentapp.presentation.util.navigation.Screen
import com.dviltres.paymentapp.presentation.util.navigation.StandardScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaymentAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scaffoldState = rememberScaffoldState()
                    val navController = rememberNavController()

                    StandardScaffold(
                        scaffoldState = scaffoldState,
                        drawerGesturesEnabled = false,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        NavigationHost(
                            navController =  navController,
                            startDestination = Screen.PaymentScreen.route
                        )
                    }
                }
            }
        }
    }
}

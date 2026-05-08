package com.example.mindfuel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindfuel.ui.screens.history.HistoryScreen
import com.example.mindfuel.ui.screens.insight.InsightScreen
import com.example.mindfuel.ui.screens.login.LoginScreen
import com.example.mindfuel.ui.screens.signup.SignupScreen
import com.example.mindfuel.ui.screens.loginmodel.Loginviewmodel
import com.example.mindfuel.ui.screens.main.MainScreen
import com.example.mindfuel.ui.screens.splash.SplashScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        
        composable(ROUT_LOGIN) {
            val viewModel: Loginviewmodel = viewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(ROUT_MAIN) {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(ROUT_SIGNUP)
                }
            )
        }
        
        composable(ROUT_SIGNUP) {
            val viewModel: Loginviewmodel = viewModel()
            SignupScreen(
                viewModel = viewModel,
                onSignupSuccess = {
                    navController.navigate(ROUT_MAIN) {
                        popUpTo(ROUT_SIGNUP) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(ROUT_MAIN) {
            MainScreen(navController)
        }
        
        composable(ROUT_HISTORY) {
            HistoryScreen(navController)
        }
        
        composable(ROUT_INSIGHT) {
            InsightScreen(navController)
        }
    }
}

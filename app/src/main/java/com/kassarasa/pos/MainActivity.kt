package com.kassarasa.pos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.screens.BerandaScreen
import com.kassarasa.pos.screens.DashboardScreen
import com.kassarasa.pos.screens.KelolaMenuScreen
import com.kassarasa.pos.screens.LaporanScreen
import com.kassarasa.pos.screens.LoginScreen
import com.kassarasa.pos.screens.PembayaranScreen
import com.kassarasa.pos.screens.RegisterScreen
import com.kassarasa.pos.screens.RiwayatTransaksiScreen
import com.kassarasa.pos.screens.TransaksiScreen
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.theme.KassaRasaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KassaRasaTheme {
                KassaRasaApp()
            }
        }
    }
}

@Composable
fun KassaRasaApp() {
    val navController = rememberNavController()
    val viewModel = remember { KassaRasaViewModel() }

    val navigateToMainTab: (AppRoute) -> Unit = { target ->
        navController.navigate(target.route) {
            popUpTo(AppRoute.Dashboard.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val handleLogout: () -> Unit = {
        navController.navigate(AppRoute.Beranda.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoute.Beranda.route
    ) {
        composable(AppRoute.Beranda.route) {
            BerandaScreen(
                onNavigateToLogin = { navController.navigate(AppRoute.Login.route) },
                onNavigateToRegister = { navController.navigate(AppRoute.Register.route) }
            )
        }

        composable(AppRoute.Login.route) {
            LoginScreen(
                onLoginAttempt = { id, pass -> viewModel.login(id, pass) },
                onLoginSuccess = {
                    navController.navigate(AppRoute.Dashboard.route) {
                        popUpTo(AppRoute.Beranda.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToBeranda = {
                    navController.popBackStack(AppRoute.Beranda.route, inclusive = false)
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoute.Register.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoute.Register.route) {
            RegisterScreen(
                onRegisterAttempt = { email, pass, confirm -> viewModel.register(email, pass, confirm) },
                onRegisterSuccess = {
                    navController.navigate(AppRoute.Dashboard.route) {
                        popUpTo(AppRoute.Beranda.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(AppRoute.Beranda.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoute.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigate = navigateToMainTab,
                onLogout = handleLogout
            )
        }

        composable(AppRoute.KelolaMenu.route) {
            KelolaMenuScreen(
                viewModel = viewModel,
                onNavigate = navigateToMainTab,
                onLogout = handleLogout
            )
        }

        composable(AppRoute.Transaksi.route) {
            TransaksiScreen(
                viewModel = viewModel,
                onNavigateToBeranda = { navigateToMainTab(AppRoute.Dashboard) },
                onNavigateToCheckout = { navController.navigate(AppRoute.Pembayaran.route) },
                onLogout = handleLogout
            )
        }

        composable(AppRoute.Pembayaran.route) {
            PembayaranScreen(
                viewModel = viewModel,
                onNavigate = navigateToMainTab,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoute.Riwayat.route) {
            RiwayatTransaksiScreen(
                viewModel = viewModel,
                onNavigate = navigateToMainTab
            )
        }

        composable(AppRoute.Laporan.route) {
            LaporanScreen(
                viewModel = viewModel,
                onNavigate = navigateToMainTab,
                onLogout = handleLogout
            )
        }
    }
}

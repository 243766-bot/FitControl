package com.example.fitcontrol.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.fitcontrol.feature_auth.presentation.login.LoginScreen
import com.example.fitcontrol.feature_auth.presentation.register.RegisterScreen
import com.example.fitcontrol.feature_members.presentation.add_member.AddMemberScreen
import com.example.fitcontrol.feature_members.presentation.edit_member.EditMemberScreen
import com.example.fitcontrol.feature_members.presentation.home.HomeScreen
import com.example.fitcontrol.feature_members.presentation.inventory.InventoryScreen
import com.example.fitcontrol.feature_members.presentation.payment.PaymentScreen
import com.example.fitcontrol.feature_members.presentation.reports.ReportScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    onMemberSavedVibrate: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login,
        modifier = Modifier.padding(padding)
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToAddMember = { navController.navigate(Screen.AddMember) },
                onNavigateToPayment = { id, name -> navController.navigate(Screen.Payment(id, name)) },
                onNavigateToInventory = { navController.navigate(Screen.Inventory) },
                onNavigateToReports = { navController.navigate(Screen.Reports) },
                onNavigateToEditMember = { id -> navController.navigate(Screen.EditMember(id)) },
                // ACTUALIZACIÓN: Lógica para cerrar sesión y limpiar el historial
                onLogoutNavigate = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.AddMember> {
            AddMemberScreen(
                onNavigateBack = { navController.popBackStack() },
                onMemberSaved = {
                    onMemberSavedVibrate()
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.EditMember> { backStackEntry ->
            val edit: Screen.EditMember = backStackEntry.toRoute()
            EditMemberScreen(
                memberId = edit.memberId,
                onNavigateBack = { navController.popBackStack() },
                onMemberUpdated = { navController.popBackStack() }
            )
        }
        composable<Screen.Payment> { backStackEntry ->
            val payment: Screen.Payment = backStackEntry.toRoute()
            PaymentScreen(
                memberId = payment.memberId,
                memberName = payment.memberName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Inventory> {
            InventoryScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable<Screen.Reports> {
            ReportScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

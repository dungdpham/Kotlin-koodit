package com.example.labfinal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.labfinal.ui.screens.MPInfoView
import com.example.labfinal.ui.screens.PartiesView
import com.example.labfinal.ui.screens.PartyMembersView

// Dung Pham 2217963 13.10.24
// Composable that manages the app's navigation flow. 3 routes are defined: parties_screen,
// partymembers_screen, and mp_screen, which are mapped to corresponding screens/views. User can
// navigate between screens by selecting onscreen clickable items.
// Flow from the starting screen: parties_screen -> partymembers_screen -> mp_screen
@Composable
fun NavigationController() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "parties_screen") {
        composable("parties_screen") {
            PartiesView(navController)
        }

        composable(
            "partymembers_screen/{party}",
            arguments = listOf(navArgument("party") { type = NavType.StringType })
        ) { backStackEntry ->
            val party = backStackEntry.arguments?.getString("party") ?: ""
            PartyMembersView(navController, party)
        }

        composable(
            "mp_screen/{hetekaId}",
            arguments = listOf(navArgument("hetekaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hetekaId = backStackEntry.arguments?.getInt("hetekaId") ?: 0
            MPInfoView(navController, hetekaId)
        }
    }
}
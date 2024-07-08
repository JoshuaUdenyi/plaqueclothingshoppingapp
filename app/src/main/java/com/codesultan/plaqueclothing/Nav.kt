package com.codesultan.plaqueclothing

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codesultan.plaqueclothing.ui.HomeScreen
import com.codesultan.plaqueclothing.ui.ViewProductDetail


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable(route = "home") {
            HomeScreen(navController)

        }
        composable(
            route = "details" + "?id={id}&price={price}", arguments = listOf(
                navArgument(
                    name = "id"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(
                    name = "price"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                },

                )
        ) {

            ViewProductDetail(navController = navController)

        }

    }

}
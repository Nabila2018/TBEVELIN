@file:Suppress("DEPRECATION")

package com.example.evelin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evelin.ui.login.LoginScreen
import com.example.evelin.ui.register.RegisterScreen
import com.example.evelin.ui.home.HomeScreen
import com.example.evelin.data.pref.UserPreference
import com.example.evelin.data.pref.dataStore
import com.example.evelin.ui.PusherService
import com.example.evelin.ui.addEvent.AddEventScreen
import com.example.evelin.ui.editEvent.EditEventScreen
import com.example.evelin.ui.eventDetail.EventDetailsScreen
import com.example.evelin.ui.history.EventHistoryScreen
import com.example.evelin.ui.myEvent.MyEventScreen
import com.example.evelin.ui.home.SearchResultsScreen

import com.example.evelin.ui.profile.ProfileScreen
import com.example.evelin.ui.registerEvent.RegisterEventScreen
import com.example.evelin.ui.userInfo.UserInfoScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    private lateinit var pusherService: PusherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Pusher service
        pusherService = PusherService(this)
        pusherService.initialize()

        setContent {
            EvelinApp()
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EvelinApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreference = UserPreference.getInstance(context.dataStore)
    val userSession = userPreference.getSession().collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userSession.value) {
        val isLoggedIn = userSession.value?.isLogin == true
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
        Log.d("EvelinApp", "isLoggedIn: $isLoggedIn")
    }

    AnimatedNavHost(navController = navController, startDestination = if (userSession.value?.isLogin == true) "home" else "login") {
        composable(
            "login",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { LoginScreen(context, navController) }
        composable(
            "register",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { RegisterScreen(navController) }
        composable(
            "home",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { HomeScreen(navController = navController) }
        composable(
            "profile",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { ProfileScreen(navController) }
        composable(
            "addEvent",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { AddEventScreen(navController) }
        composable(
            "userInfo",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { UserInfoScreen(onBackClick = { navController.popBackStack() }) }
        composable(
            "eventHistory",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { EventHistoryScreen(navController = navController, onBackClick = { navController.popBackStack() }) }
        composable(
            "myEvent",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { MyEventScreen(navController = navController, onBackClick = { navController.popBackStack() }) }
        composable(
            "eventDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("id")
            EventDetailsScreen(navController = navController, eventId = eventId)
        }
        composable(
            "editEvent/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("id")
            EditEventScreen(navController = navController, eventId = eventId.toString())
        }
        composable(
            "registerEvent/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType }),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            RegisterEventScreen(
                onBackClick = { navController.popBackStack() },
                eventId = eventId,
                navController = navController
            )
        }
        composable("searchResults/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(navController = navController, query = query)
        }
    }
}
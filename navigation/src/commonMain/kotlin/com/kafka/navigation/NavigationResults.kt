package com.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LoginNavigationResult(navController: NavController, onLogin: () -> Unit) {
//    val currentEntry = navController.currentBackStackEntryAsState().value
//    val loginResult by currentEntry!!.savedStateHandle
//        .getStateFlow(Screen.Login.result, false)
//        .collectAsStateWithLifecycle(false)
//
//    LaunchedEffect(loginResult) {
//        if (loginResult) {
//            currentEntry!!.savedStateHandle.remove<Boolean>(Screen.Login.result)
//            onLogin()
//        }
//    }
}
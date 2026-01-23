package dev.eliaschen.noti.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

val LocalNavStack = compositionLocalOf<NavBackStack<NavKey>> { error("NavBackStack") }
val LocalSnackBarHostState = compositionLocalOf<SnackbarHostState> { error("Snackbar") }
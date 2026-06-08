package com.decoutkhanqindev.lich_viet_loc_phat.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.CalendarScreen
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.SettingsScreen
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.TodayScreen

@Composable
fun AppNavDisplay(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        entries = rememberDecoratedNavEntries(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<TodayDestination> { key ->
                    TodayScreen(initialDate = key.toSolarDate())
                }
                entry<CalendarDestination> {
                    CalendarScreen(backStack)
                }
                entry<SettingsDestination> {
                    SettingsScreen()
                }
            },
        ),
        modifier = modifier,
        transitionSpec = {
            tabSlide(tabIndexOf(targetState.key) >= tabIndexOf(initialState.key))
        },
        popTransitionSpec = {
            tabSlide(tabIndexOf(targetState.key) >= tabIndexOf(initialState.key))
        },
        predictivePopTransitionSpec = {
            tabSlide(tabIndexOf(targetState.key) >= tabIndexOf(initialState.key))
        },
        onBack = {
            backStack.removeLastOrNull()
        },
    )
}

private fun tabIndexOf(sceneKey: Any): Int = when {
    sceneKey.toString().startsWith("CalendarDestination") -> 1
    sceneKey.toString().startsWith("SettingsDestination") -> 2
    else -> 0
}

private fun tabSlide(forward: Boolean): ContentTransform = (
        slideInHorizontally(tween(300)) { full -> if (forward) full else -full } +
                fadeIn(tween(300))
        ) togetherWith (
        slideOutHorizontally(tween(300)) { full -> if (forward) -full else full } +
                fadeOut(tween(300))
        )

package com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation

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
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.CalendarScreen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.SettingsScreen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.today.TodayScreen

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
                entry<SplashDestination> {
                    SplashScreen(
                        onNavigateToToday = {
                            backStack.navigateTo(TodayDestination(), false)
                        }
                    )
                }
                entry<TodayDestination> { key -> TodayScreen(initialDate = key.toSolarDate()) }
                entry<CalendarDestination> { CalendarScreen(onNavigateToTab = backStack::navigateTo) }
                entry<SettingsDestination> { SettingsScreen() }
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
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
    )
}

fun NavBackStack<NavKey>.navigateTo(destination: NavKey, preserveState: Boolean = true) {
    val existingIndex = indexOfFirst { it::class == destination::class }

    if (preserveState) {
        if (existingIndex >= 0) {
            while (size > existingIndex + 1) removeLastOrNull()
        } else {
            val root = first()
            clear()
            add(root)
            if (destination::class != root::class) add(destination)
        }
    } else {
        clear()
        add(destination)
    }
}

private fun tabIndexOf(sceneKey: Any): Int = when {
    sceneKey.toString().startsWith(CalendarDestination::class.java.simpleName) -> 1
    sceneKey.toString().startsWith(SettingsDestination::class.java.simpleName) -> 2
    else -> 0
}

private fun tabSlide(forward: Boolean): ContentTransform =
    (slideInHorizontally(tween(300)) { full -> if (forward) full else -full } + fadeIn(tween(300))) togetherWith (
            slideOutHorizontally(tween(300)) { full -> if (forward) -full else full } + fadeOut(
                tween(300)
            ))

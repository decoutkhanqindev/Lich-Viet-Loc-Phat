package com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation

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
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.today.TodayScreen
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo
import com.decoutkhanqindev.lich_viet_loc_phat.utils.tabIndexOf
import com.decoutkhanqindev.lich_viet_loc_phat.utils.tabSlide

@Composable
fun MainNavDisplay(
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
                entry<TodayDestination> { dest ->
                    TodayScreen(initialDate = dest.toSolarDate())
                }
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
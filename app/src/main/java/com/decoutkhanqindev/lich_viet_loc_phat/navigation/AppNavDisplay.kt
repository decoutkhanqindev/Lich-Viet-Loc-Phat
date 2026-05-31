package com.decoutkhanqindev.lich_viet_loc_phat.navigation

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
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.ConverterScreen
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
                entry<CalendarDestination> { CalendarScreen(backStack) }
                entry<ConverterDestination> { ConverterScreen() }
                entry<SettingsDestination> { SettingsScreen() }
            },
        ),
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
    )
}
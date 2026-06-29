package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.MainActivity
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ObserveOnLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads.AdLoadingDialog
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.toTodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarIntent
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun CalendarScreen(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    val activity = context as MainActivity
    val viewModel: CalendarViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val adsManager: AdsManager = koinInject()
    val rewardWidgetState by adsManager.rewardWidget.state.collectAsStateWithLifecycle()

    LaunchedEffect(rewardWidgetState) {
        if (rewardWidgetState == AdUnitState.LOADED) {
            adsManager.rewardWidget.show(
                activity = activity,
                onEarned = {
                    viewModel.onIntent(CalendarIntent.AddWidget)
                },
                onAdClosed = {
                    viewModel.onIntent(CalendarIntent.DismissWidgetBottomSheet)
                },
                onAdFailedToShow = {
                    viewModel.onIntent(CalendarIntent.DismissWidgetBottomSheet)
                },
            )
        }
    }

    ObserveOnLifecycleOwner {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CalendarEffect.NavigateToToday -> backStack.navigateTo(effect.date.toTodayDestination())
                is CalendarEffect.WatchAdToAddWidget -> adsManager.rewardWidget.load(context)
                is CalendarEffect.ShowMessage -> activity.showToast(effect.messageRes)
            }
        }
    }

    if (rewardWidgetState == AdUnitState.LOADING) AdLoadingDialog()

    CalendarContent(state = state, onIntent = viewModel::onIntent)
}

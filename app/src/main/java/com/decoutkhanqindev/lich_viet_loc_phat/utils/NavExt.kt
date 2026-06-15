package com.decoutkhanqindev.lich_viet_loc_phat.utils

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.CalendarDestination
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.SettingsDestination

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

fun tabIndexOf(sceneKey: Any): Int = when {
    sceneKey.toString().startsWith(CalendarDestination::class.java.simpleName) -> 1
    sceneKey.toString().startsWith(SettingsDestination::class.java.simpleName) -> 2
    else -> 0
}

fun tabSlide(forward: Boolean): ContentTransform =
    (slideInHorizontally(tween(300)) { full -> if (forward) full else -full } + fadeIn(tween(300))) togetherWith (
            slideOutHorizontally(tween(300)) { full -> if (forward) -full else full } + fadeOut(
                tween(300)
            ))
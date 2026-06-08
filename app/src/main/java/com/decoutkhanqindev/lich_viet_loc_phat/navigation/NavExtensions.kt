package com.decoutkhanqindev.lich_viet_loc_phat.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateToTab(destination: NavKey) {
    val existingIndex = indexOfFirst { it::class == destination::class }

    if (existingIndex >= 0) {
        while (size > existingIndex + 1) removeLastOrNull()
    } else {
        val root = first()
        clear()
        add(root)
        if (destination::class != root::class) add(destination)
    }
}
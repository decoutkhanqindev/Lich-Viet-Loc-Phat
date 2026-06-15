package com.decoutkhanqindev.lich_viet_loc_phat.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(destination: NavKey, preserveState: Boolean = true) {
    val existingIndex = indexOfFirst { it::class == destination::class }

    if (preserveState) {
        if (existingIndex >= 0) {
            while (size > existingIndex + 1) removeLastOrNull()
            if (last() != destination) {
                removeLastOrNull()
                add(destination)
            }
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
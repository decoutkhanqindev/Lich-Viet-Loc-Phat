package com.decoutkhanqindev.lich_viet_loc_phat.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateToTab(destination: NavKey) {
    val existingIndex = indexOfFirst { it::class == destination::class }

    if (existingIndex >= 0) {
        // Tab đã tồn tại → pop về đúng vị trí (giữ state tab)
        while (size > existingIndex + 1) removeLastOrNull()
    } else {
        // Tab chưa có → reset về root rồi push tab mới
        val root = first()
        clear()
        add(root)
        if (destination::class != root::class) add(destination)
    }
}
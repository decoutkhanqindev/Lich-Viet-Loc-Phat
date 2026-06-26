package com.decoutkhanqindev.lich_viet_loc_phat.utils

interface Tag {
    val tag: String get() = this::class.simpleName ?: ""
}
package com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads

enum class AdUnitState {
    NONE, LOADING, LOADED, FAILED, IMPRESSION;

    companion object {
        const val FAILED_DELAY_DURATION = 1500L
        const val IMPRESSION_DELAY_DURATION = 2500L
    }
}

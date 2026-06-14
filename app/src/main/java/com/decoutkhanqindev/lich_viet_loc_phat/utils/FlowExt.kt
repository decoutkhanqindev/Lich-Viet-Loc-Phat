package com.decoutkhanqindev.lich_viet_loc_phat.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.toFlowResult(): Flow<Result<T>> = try {
    map { Result.success(it) }
} catch (e: Exception) {
    flowOf(Result.failure(e))
}
package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

fun Modifier.onClick(
    shape: Shape = CircleShape,
    ripple: Boolean = true,
    action: () -> Unit,
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "OnScalableClickScaleAnimation"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(shape)
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                isPressed = true
                waitForUpOrCancellation()
                isPressed = false
            }
        }
        .then(
            if (ripple) {
                this.clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = action,
                )
            } else {
                this.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = action,
                )
            }
        )
}

@Composable
fun Modifier.shimmer(
    isEnable: Boolean = true,
    durationMillis: Int = 1000,
    bounds: ShimmerBounds = ShimmerBounds.View,
): Modifier {
    if (!isEnable) return this

    val theme = remember(durationMillis) {
        defaultShimmerTheme.copy(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            )
        )
    }
    val shimmer = rememberShimmer(shimmerBounds = bounds, theme = theme)

    return this.shimmer(customShimmer = shimmer)
}
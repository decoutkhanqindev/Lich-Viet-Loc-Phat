package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
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
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = action,
                )
            } else {
                Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = action,
                )
            }
        )
}
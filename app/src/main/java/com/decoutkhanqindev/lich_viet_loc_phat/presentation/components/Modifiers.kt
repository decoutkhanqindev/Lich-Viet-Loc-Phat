package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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



private val ShimmerCosA = cos((20.0 * PI / 180.0).toFloat())
private val ShimmerSinA = sin((20.0 * PI / 180.0).toFloat())
private val LoadingShimmerHighlight = Color.White.copy(alpha = 0.35f)

private fun DrawScope.drawShimmerGradient(progress: Float, highlightColor: Color) {
    val bandHalf = 100f
    val cx = size.width / 2f
    val cy = size.height / 2f
    val bx = progress * (size.width + bandHalf * 4) - bandHalf * 2
    val startRelX = bx - bandHalf - cx
    val endRelX = bx + bandHalf - cx

    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, highlightColor, Color.Transparent),
            start = Offset(startRelX * ShimmerCosA + cx, startRelX * ShimmerSinA + cy),
            end = Offset(endRelX * ShimmerCosA + cx, endRelX * ShimmerSinA + cy),
        ),
    )
}

@Composable
fun Modifier.shimmerLoading(
    isEnable: Boolean = true,
    durationMillis: Int = 1000,
): Modifier {
    if (!isEnable) return this

    val transition = rememberInfiniteTransition(label = "ShimmerLoading")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ShimmerLoadingProgress",
    )

    return this.drawWithContent {
        drawContent()
        drawShimmerGradient(progress, LoadingShimmerHighlight)
    }
}

@Composable
fun Modifier.shimmerHighlight(
    backgroundColor: Color,
    highlightColor: Color,
    durationMillis: Int = 1400,
): Modifier {
    val transition = rememberInfiniteTransition(label = "ShimmerHighlight")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ShimmerHighlightProgress",
    )
    return this.drawWithContent {
        drawRect(backgroundColor)
        drawShimmerGradient(progress, highlightColor)
        drawContent()
    }
}
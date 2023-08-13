package com.sanathcoding.rotatebutton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun RotateButtonComposable() {
    var touched by remember {
        mutableStateOf(false)
    }
    val s = remember { Color(0xFFA0A0A0) }
    val e = remember { Color(0xFF7A75AD) }

    val startColor by animateColorAsState(
        targetValue = if (touched) s else e,
        animationSpec = tween(500),
        label = "Start Color Animation"
    )

    val endColor by animateColorAsState(
        targetValue = if (touched) e else s,
        animationSpec = tween(500),
        label = "End Color Animation"
    )

    val scale by animateFloatAsState(
        targetValue = if (touched) .94f else 1f,
        animationSpec = spring(
            Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Scale Animation"
    )

    var rotY by remember { mutableStateOf(0f) }
    var rotX by remember { mutableStateOf(0f) }

    val animatedRotationY by animateFloatAsState(
        targetValue = rotY,
        animationSpec = spring(
            Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "RotationY Animation"
    )

    val animatedRotationX by animateFloatAsState(
        targetValue = rotX,
        animationSpec = spring(
            Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "RotationX Animation"
    )

    var size by remember { mutableStateOf(Size.Zero) }
    
    Box(
        modifier = Modifier
            .padding(4.dp)
            .onSizeChanged { size = it.toSize() }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offSet ->
                        val xPos = ((offSet.x / size.width) * 2) - 1f
                        val yPos = ((offSet.y / size.height) * -2) + 1f

                        val rot = 10
                        rotY = xPos * rot
                        rotX = yPos * rot * 2

                        touched = true
                        awaitRelease()
                        touched = false

                        rotY = 0f
                        rotX = 0f
                    }
                )
            }
            .scale(scale)
            .graphicsLayer {
                rotationX = animatedRotationX
                rotationY = animatedRotationY
            }
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    listOf(
                        startColor,
                        endColor
                    ),
                    Offset(0.0f, 0.0f),
                    Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = CircleShape
            )
            .background(
                color = Color.Black,
                shape = CircleShape
            )
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        CompositionLocalProvider (
            LocalContentColor provides Color.White,
            LocalTextStyle provides TextStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            Text(text = "Rotate Button")
        }
    }
}
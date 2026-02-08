package io.wally.me.admin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.wally.me.admin.ui.theme.GlassWhite
import io.wally.me.admin.ui.theme.NeonBlue
import io.wally.me.admin.ui.theme.NeonPurple

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(GlassWhite)
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(listOf(NeonBlue.copy(alpha = 0.5f), NeonPurple.copy(alpha = 0.5f))),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(1.dp),
        content = content
    )
}

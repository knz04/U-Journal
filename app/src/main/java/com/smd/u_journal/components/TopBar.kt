package com.smd.u_journal.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100

@Composable
fun TopBar(isExpanded: Boolean, onCloseClick: () -> Unit) {
    // Animate width transition
    val barWidth by animateDpAsState(
        targetValue = if (isExpanded) 360.dp else 172.dp, // Expands smoothly
        animationSpec = tween(durationMillis = 500), // 300ms smooth transition
        label = "Bar Width Animation"
    )

    // Animate profile picture fade
    val profileAlpha by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 1f, // Fades out/in
        animationSpec = tween(durationMillis = 500), // Same duration as width change
        label = "Profile Picture Fade"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(barWidth) // Smooth width transition
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Black),
            contentAlignment = Alignment.CenterStart
        ) {
            if (isExpanded) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    tint = Blue100,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable { onCloseClick() }
                )
            }
        }

        // Profile picture fades in/out
        Image(
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = "User Profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .alpha(profileAlpha), // Apply fade animation
            contentScale = ContentScale.Crop
        )
    }
}


@Preview(showBackground = true, name = "Collapsed State")
@Composable
fun TopBarCollapsedPreview() {
    Surface {
        TopBar(isExpanded = false, onCloseClick = {})
    }
}

@Preview(showBackground = true, name = "Expanded State")
@Composable
fun TopBarExpandedPreview() {
    Surface {
        TopBar(isExpanded = true, onCloseClick = {})
    }
}


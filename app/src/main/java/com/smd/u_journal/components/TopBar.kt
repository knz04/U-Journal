package com.smd.u_journal.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(isExpanded: Boolean, onCloseClick: () -> Unit) {
    SideEffect {
        Log.d("TopBar", "Recomposing with isExpanded = $isExpanded")
    }

    val barWidth by animateDpAsState(
        targetValue = if (isExpanded) 360.dp else 172.dp,
        animationSpec = tween(durationMillis = 500),
        label = "Bar Width Animation"
    )

    val profileAlpha by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 1f,
        animationSpec = tween(durationMillis = 500),
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
                .width(barWidth)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Black),
            contentAlignment = Alignment.Center
        ) {
            if (isExpanded) {
                // Format today's date as "4 April 2025"
                val formattedDate = remember {
                    val today = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
                    today.format(formatter)
                }
                Text(
                    text = formattedDate,
                    color = Blue100,
                    fontSize = 14.sp
                )

                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    tint = Blue100,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                        .clickable { onCloseClick() }
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = "User Profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .alpha(profileAlpha),
            contentScale = ContentScale.Crop
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Collapsed State")
@Composable
fun TopBarCollapsedPreview() {
    Surface {
        TopBar(isExpanded = false, onCloseClick = {})
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Expanded State")
@Composable
fun TopBarExpandedPreview() {
    Surface {
        TopBar(isExpanded = true, onCloseClick = {})
    }
}

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
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100
import com.smd.u_journal.viewmodel.TopBarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(
    state: TopBarState,
    onCloseClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val isCollapsed = state == TopBarState.COLLAPSED

    val barWidth by animateDpAsState(
        targetValue = if (isCollapsed) 172.dp else 360.dp,
        animationSpec = tween(durationMillis = 500),
        label = "Bar Width Animation"
    )

    val profileAlpha by animateFloatAsState(
        targetValue = if (isCollapsed) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "Profile Alpha Animation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state == TopBarState.ENTRY_NAV) {
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Black)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Blue100,
                        modifier = Modifier.clickable { onBackClick() }
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "Share",
                            tint = Blue100,
                            modifier = Modifier.clickable { onImageClick() }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = "Favorite",
                            tint = Blue100,
                            modifier = Modifier.clickable { onFavoriteClick() }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more),
                            contentDescription = "More",
                            tint = Blue100,
                            modifier = Modifier.clickable { onMenuClick() }
                        )
                    }
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Black),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    TopBarState.EXPANDED -> {
                        val date = remember {
                            val today = LocalDate.now()
                            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
                            today.format(formatter)
                        }
                        Text(text = date, color = Blue100, fontSize = 14.sp)
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

                    TopBarState.NEW_ENTRY -> {
                        Text(text = "New Entry", color = Blue100, fontSize = 14.sp)
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

                    TopBarState.COLLAPSED -> {
                        Text(
                            text = "U-Journal",
                            style = MaterialTheme.typography.titleMedium,
                            color = Bg100
                        )
                    }

                    else -> {}
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
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Collapsed State")
@Composable
fun TopBarCollapsedPreview() {
    Surface {
        TopBar(state = TopBarState.ENTRY_NAV, onCloseClick = {})
    }
}

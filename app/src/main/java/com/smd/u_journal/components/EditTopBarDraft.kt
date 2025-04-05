package com.smd.u_journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100

@Composable
fun TopBar(onBackClick: () -> Unit, onImageClick: () -> Unit, onFavoriteClick: () -> Unit, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Black)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Blue100,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Image Button
                    Icon(
                        painter = painterResource(id = R.drawable.media),
                        contentDescription = "Insert Image",
                        tint = Blue100,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onImageClick() }
                    )

                    // Favorite Button
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Favorite",
                        tint = Blue100,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onFavoriteClick() }
                    )

                    // Menu Button
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Menu",
                        tint = Blue100,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onMenuClick() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "TopBar Preview")
@Composable
fun TopBarPreview() {
    Surface {
        TopBar(
            onBackClick = {},
            onImageClick = {},
            onFavoriteClick = {},
            onMenuClick = {}
        )
    }
}
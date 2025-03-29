package com.smd.u_journal.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Ensures items are at opposite ends
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Black Rounded Rectangle
        Box(
            modifier = Modifier
                .width(172.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Black)
        )

        // Right: Profile Picture
        Image(
            painter = painterResource(id = R.drawable.profile_placeholder), // Replace with actual image
            contentDescription = "User Profile",
            modifier = Modifier
                .size(40.dp) // Match the Figma height
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    Surface {
        TopBar()
    }
}

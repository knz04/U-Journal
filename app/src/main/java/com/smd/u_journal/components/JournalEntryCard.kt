package com.smd.u_journal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.smd.u_journal.R

@Composable
fun JournalEntryCard(
    title: String,
    subtitle: String,
    dateLabel: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardColor = if (isPressed) Color(0xFF1F1F1F) else Color.White
    val textColorPrimary = if (isPressed) Color(0xFF40C2FF) else Color.Black
    val textColorSecondary = if (isPressed) Color.LightGray else Color.DarkGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, Color(0xFFCACACA), shape = RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)


    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kiri: Teks
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = textColorSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColorPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    color = textColorSecondary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Kanan: Gambar
            Image(
                painter = painterResource(id = R.drawable.dummy1),
                contentDescription = "Journal Image",
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun JournalEntryCardPreview() {
    JournalEntryCard(
        title = "Chimpanzini Bananini",
        subtitle = "Kisah Hidup Anomali Monyet",
        dateLabel = "Recently Opened",
        onClick = {} // kosongin aja buat preview
    )
}

//        // Dalam LazyColumn item:
//        item {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .border(1.dp, Color(0xFFCACACA), shape = RoundedCornerShape(16.dp)), // tambahin border di sini
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Kiri: Teks
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .padding(end = 8.dp)
//                    ) {
//                        Text(
//                            text = "Recently Opened",
//                            style = MaterialTheme.typography.bodySmall,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.DarkGray
//                        )
//
//                        Spacer(modifier = Modifier.height(14.dp))
//
//                        Text(
//                            text = "Chimpanzini Bananini",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//
//                        Spacer(modifier = Modifier.height(14.dp))
//
//                        Text(
//                            text = "Kisah Hidup Anomali Monyet",
//                            style = MaterialTheme.typography.bodySmall,
//                            fontWeight = FontWeight.Normal,
//                            color = Color.DarkGray
//                        )
//                    }
//
//                    // Kanan: Gambar
//                    Image(
//                        painter = painterResource(id = R.drawable.dummy1),
//                        contentDescription = "Journal Image",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(RoundedCornerShape(12.dp))
//                    )
//                }
//            }
//        }


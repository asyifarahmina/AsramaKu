package com.example.asramaku.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun StatusPembayaranRow(no: Int, bulan: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(no.toString(), 0.15f)
        TableCell(bulan, 0.45f)
        StatusCell(status, 0.4f)
    }
}

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier
            .weight(weight)
            .padding(4.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RowScope.StatusCell(status: String, weight: Float) {
    val warnaBg = if (status.lowercase() == "lunas") Color(0xFF2E5D5A) else Color(0xFF7C8A87)
    val warnaText = Color.White

    Box(
        modifier = Modifier
            .weight(weight)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = warnaBg,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = status,
                color = warnaText,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

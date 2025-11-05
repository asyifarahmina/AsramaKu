package com.example.asramaku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailPembayaranScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detail Pembayaran", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Text("Nama Tagihan: Sewa Kamar")
        Text("Jumlah: Rp 500.000")
        Text("Tanggal Bayar: 12 Okt 2025")
        Text("Status: Lunas")
    }
}

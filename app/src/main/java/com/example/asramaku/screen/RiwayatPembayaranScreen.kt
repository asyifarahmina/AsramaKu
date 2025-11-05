package com.example.asramaku.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.asramaku.component.RiwayatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPembayaranScreen(
    onBackClick: () -> Unit = {},
    riwayatList: List<Triple<String, String, String>> = emptyList() // (bulan, jumlah, status)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0D5))
    ) {
        TopAppBar(
            title = { Text("Riwayat Pembayaran") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFAED6D3))
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (riwayatList.isEmpty()) {
                Text("Belum ada riwayat pembayaran.")
            } else {
                riwayatList.forEach { (bulan, jumlah, status) ->
                    RiwayatCard(
                        bulanTagihan = bulan,
                        jumlahTagihan = jumlah,
                        status = if (status == "Lunas") "Lunas" else "Belum Lunas",
                        onDetailClick = {
                            // aksi ketika tombol Lihat detail diklik
                            println("Detail pembayaran $bulan diklik")
                        }
                    )
                }
            }
        }
    }
}

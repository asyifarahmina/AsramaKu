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
import androidx.navigation.NavController
import com.example.asramaku.component.TagihanCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarTagihanScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0D5))
    ) {
        TopAppBar(
            title = { Text("Lihat Tagihan Pembayaran") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFAED6D3),
                titleContentColor = Color.Black
            )
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Data dummy sementara
            val tagihanList = listOf("November", "Desember")

            tagihanList.forEach { bulan ->
                TagihanCard(
                    bulan = bulan,
                    nama = "Asyifa",
                    noKamar = "A203",
                    totalTagihan = "Rp.500.000",
                    onBayarClick = {
                        navController.navigate("konfirmasi_pembayaran")
                    }
                )
            }
        }
    }
}

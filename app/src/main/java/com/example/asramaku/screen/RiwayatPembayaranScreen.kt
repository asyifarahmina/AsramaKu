package com.example.asramaku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.asramaku.PembayaranData
import com.example.asramaku.component.RiwayatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPembayaranScreen(
    onBackClick: () -> Unit,
    riwayatList: MutableList<PembayaranData>,
    onDetailClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit // ✅ diperbaiki dari Function<Unit>
) {
    val listState = remember { mutableStateListOf<PembayaranData>() }
    listState.clear()
    listState.addAll(riwayatList)

    var showDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pembayaran") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFA8C9C4),
                    titleContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (listState.isEmpty()) {
                Text("Belum ada riwayat pembayaran.")
            } else {
                LazyColumn {
                    itemsIndexed(listState) { index, pembayaran ->
                        RiwayatCard(
                            bulanTagihan = pembayaran.bulan,
                            jumlahTagihan = pembayaran.totalTagihan,
                            status = pembayaran.status,
                            onDetailClick = { onDetailClick(index) },
                            onDeleteClick = {
                                selectedIndex = index
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog konfirmasi hapus
    if (showDialog && selectedIndex >= 0) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick(selectedIndex)
                    listState.removeAt(selectedIndex)
                    showDialog = false
                }) {
                    Text("Hapus", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus riwayat pembayaran ini?") }
        )
    }
}

package com.example.asramaku

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asramaku.screen.DaftarTagihanScreen
import com.example.asramaku.screen.RiwayatPembayaranScreen
import com.example.asramaku.screen.StatusPembayaranScreen
import com.example.app.ui.screens.KonfirmasiPembayaranScreen
import com.example.asramaku.ui.theme.AsramaKuTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsramaKuTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val context = this@MainActivity

                // 🔹 Menyimpan riwayat konfirmasi pembayaran
                val riwayatPembayaranList = remember {
                    mutableStateListOf<Triple<String, String, String>>() // (bulan, jumlah, status)
                }

                // 🔹 Daftar bulan tagihan yang tersedia
                val daftarTagihan = listOf("Oktober", "November", "Desember")

                // 🔹 State sementara untuk input form (biar bisa divalidasi dari MainActivity)
                var nama by remember { mutableStateOf("") }
                var bulan by remember { mutableStateOf("") }
                var noKamar by remember { mutableStateOf("") }
                var totalTagihan by remember { mutableStateOf("") }

                val items = listOf(
                    BottomNavItem("Tagihan", "daftar_tagihan", Icons.Filled.List),
                    BottomNavItem("Konfirmasi", "konfirmasi_pembayaran", Icons.Filled.Payments),
                    BottomNavItem("Status", "status_pembayaran", Icons.Filled.CheckCircle),
                    BottomNavItem("Riwayat", "riwayat_pembayaran", Icons.Filled.History)
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->

                    // 🔹 Navigasi antar halaman
                    NavHost(
                        navController = navController,
                        startDestination = "daftar_tagihan",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // 🧾 Daftar Tagihan
                        composable("daftar_tagihan") {
                            DaftarTagihanScreen(navController)
                        }

                        // 💰 Konfirmasi Pembayaran
                        composable("konfirmasi_pembayaran") {
                            // Kirim nilai input dari KonfirmasiPembayaranScreen ke state di sini
                            KonfirmasiPembayaranScreen(
                                onBackClick = { navController.popBackStack() },
                                onSubmitClick = {
                                    if (nama.isBlank() || bulan.isBlank() || noKamar.isBlank() || totalTagihan.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Harap isi semua data terlebih dahulu!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Pembayaran berhasil dikirim!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Simpan data ke riwayat pembayaran
                                        if (riwayatPembayaranList.none { it.first == bulan }) {
                                            riwayatPembayaranList.add(
                                                Triple(bulan, totalTagihan, "Lunas")
                                            )
                                        }
                                    }
                                },
                                onCancelClick = { navController.popBackStack() }
                            )

                            // TODO: Jika kamu ingin biar bisa sinkron langsung dengan field di screen,
                            // nanti bisa kita tambahkan callback untuk update state ini.
                        }

                        // 📊 Status Pembayaran
                        composable("status_pembayaran") {
                            val dataStatus = daftarTagihan.map { bulanItem ->
                                val sudahLunas = riwayatPembayaranList.any { it.first == bulanItem }
                                Triple(bulanItem, "500000", if (sudahLunas) "Lunas" else "Belum Lunas")
                            }

                            StatusPembayaranScreen(
                                navController = navController,
                                riwayatList = dataStatus
                            )
                        }

                        // 📜 Riwayat Pembayaran
                        composable("riwayat_pembayaran") {
                            RiwayatPembayaranScreen(
                                onBackClick = { navController.popBackStack() },
                                riwayatList = riwayatPembayaranList
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

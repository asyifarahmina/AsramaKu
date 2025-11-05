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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.asramaku.screen.*
import com.example.app.ui.screens.KonfirmasiPembayaranScreen
import com.example.asramaku.ui.theme.AsramaKuTheme

data class PembayaranData(
    val nama: String,
    val bulan: String,
    val noKamar: String,
    val totalTagihan: String,
    val status: String,
    val buktiUri: String? = null
)

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

                // 🔹 Data riwayat pembayaran disimpan dengan data lengkap
                val riwayatPembayaranList = remember {
                    mutableStateListOf<PembayaranData>()
                }

                val daftarTagihan = listOf("Oktober", "November", "Desember")

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
                            val contextLocal = LocalContext.current

                            KonfirmasiPembayaranScreen(
                                onBackClick = { navController.popBackStack() },
                                onSubmitClick = { nama, bulan, noKamar, totalTagihan, buktiUri ->
                                    if (
                                        nama.isBlank() ||
                                        bulan.isBlank() ||
                                        noKamar.isBlank() ||
                                        totalTagihan.isBlank() ||
                                        buktiUri == null
                                    ) {
                                        Toast.makeText(
                                            contextLocal,
                                            "Harap isi semua data terlebih dahulu!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            contextLocal,
                                            "Pembayaran berhasil dikirim!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        if (riwayatPembayaranList.none { it.bulan == bulan }) {
                                            riwayatPembayaranList.add(
                                                PembayaranData(
                                                    nama = nama,
                                                    bulan = bulan,
                                                    noKamar = noKamar,
                                                    totalTagihan = totalTagihan,
                                                    status = "Lunas",
                                                    buktiUri = buktiUri.toString()
                                                )
                                            )
                                        }

                                        navController.navigate("riwayat_pembayaran")
                                    }
                                },
                                onCancelClick = { navController.popBackStack() }
                            )
                        }

                        // 📊 Status Pembayaran
                        composable("status_pembayaran") {
                            val dataStatus = daftarTagihan.map { bulanItem ->
                                val sudahLunas = riwayatPembayaranList.any { it.bulan == bulanItem }
                                Triple(
                                    bulanItem,
                                    "500000",
                                    if (sudahLunas) "Lunas" else "Belum Lunas"
                                )
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
                                riwayatList = riwayatPembayaranList,
                                onDetailClick = { index ->
                                    navController.navigate("detail_pembayaran/$index")
                                }
                            )
                        }

                        // 📄 Detail Pembayaran (pakai argumen index)
                        composable(
                            route = "detail_pembayaran/{index}",
                            arguments = listOf(navArgument("index") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val index = backStackEntry.arguments?.getInt("index") ?: -1
                            val pembayaran = riwayatPembayaranList.getOrNull(index)
                            if (pembayaran != null) {
                                DetailPembayaranScreen(
                                    pembayaran = pembayaran,
                                    onBackClick = { navController.popBackStack() }
                                )
                            } else {
                                Text("Data tidak ditemukan")
                            }
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

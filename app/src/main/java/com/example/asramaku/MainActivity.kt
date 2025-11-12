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

// 🔹 Model Data
data class PembayaranData(
    val nama: String,
    val bulan: String,
    val noKamar: String,
    val totalTagihan: String,
    val status: String,
    val buktiUri: String? = null
)

// 🔹 Item untuk Bottom Navigation
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
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

                // 🧾 Daftar riwayat pembayaran
                val riwayatPembayaranList = remember { mutableStateListOf<PembayaranData>() }

                // 📅 Daftar bulan tagihan
                val daftarTagihan = remember { mutableStateListOf("Oktober", "November", "Desember") }

                // 🟢 Map untuk menyimpan status tiap bulan
                val statusPembayaranMap = remember {
                    mutableStateMapOf(
                        "Oktober" to "Belum Lunas",
                        "November" to "Belum Lunas",
                        "Desember" to "Belum Lunas"
                    )
                }

                // 🔹 Item bottom navigation
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
                            val tagihanBelumLunas = daftarTagihan.filter {
                                statusPembayaranMap[it] == "Belum Lunas"
                            }
                            DaftarTagihanScreen(navController, tagihanBelumLunas)
                        }

                        // 💰 Konfirmasi Pembayaran
                        composable("konfirmasi_pembayaran") {
                            KonfirmasiPembayaranScreen(
                                onBackClick = { navController.popBackStack() },
                                onSubmitClick = { _, _, _, _, _ -> },
                                onCancelClick = { navController.popBackStack() }
                            )
                        }

                        // 💰 Konfirmasi Pembayaran dengan argumen
                        composable(
                            route = "konfirmasi_pembayaran/{bulan}/{nama}/{noKamar}/{totalTagihan}",
                            arguments = listOf(
                                navArgument("bulan") { type = NavType.StringType },
                                navArgument("nama") { type = NavType.StringType },
                                navArgument("noKamar") { type = NavType.StringType },
                                navArgument("totalTagihan") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val contextLocal = LocalContext.current
                            val bulan = backStackEntry.arguments?.getString("bulan") ?: ""
                            val nama = backStackEntry.arguments?.getString("nama") ?: ""
                            val noKamar = backStackEntry.arguments?.getString("noKamar") ?: ""
                            val totalTagihan = backStackEntry.arguments?.getString("totalTagihan") ?: ""

                            KonfirmasiPembayaranScreen(
                                bulan = bulan,
                                nama = nama,
                                noKamar = noKamar,
                                totalTagihan = totalTagihan,
                                onBackClick = { navController.popBackStack() },
                                onSubmitClick = { namaInput, bulanInput, noKamarInput, totalInput, buktiUri ->
                                    if (
                                        namaInput.isBlank() ||
                                        bulanInput.isBlank() ||
                                        noKamarInput.isBlank() ||
                                        totalInput.isBlank() ||
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
                                            "Pembayaran berhasil!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        if (riwayatPembayaranList.none { it.bulan == bulanInput }) {
                                            riwayatPembayaranList.add(
                                                PembayaranData(
                                                    nama = namaInput,
                                                    bulan = bulanInput,
                                                    noKamar = noKamarInput,
                                                    totalTagihan = totalInput,
                                                    status = "Lunas",
                                                    buktiUri = buktiUri.toString()
                                                )
                                            )
                                            // 🟢 Tandai bulan sebagai LUNAS, tidak akan berubah meski dihapus
                                            statusPembayaranMap[bulanInput] = "Lunas"
                                        }

                                        navController.navigate("riwayat_pembayaran")
                                    }
                                },
                                onCancelClick = { navController.popBackStack() }
                            )
                        }

                        // 📊 Status Pembayaran
                        composable("status_pembayaran") {
                            val urutanBulan = listOf("Oktober", "November", "Desember")
                            val dataStatus = urutanBulan.map { bulanItem ->
                                val status = statusPembayaranMap[bulanItem] ?: "Belum Lunas"
                                Triple(bulanItem, "500000", status)
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
                                },
                                onDeleteClick = { deletedIndex ->
                                    if (deletedIndex in riwayatPembayaranList.indices) {
                                        // Hapus dari daftar riwayat, tapi status tetap "Lunas"
                                        riwayatPembayaranList.removeAt(deletedIndex)
                                    }
                                }
                            )
                        }

                        // 📄 Detail Pembayaran
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

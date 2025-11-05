package com.example.app.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.app.ui.components.RekeningCard
import com.example.asramaku.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KonfirmasiPembayaranScreen(
    onBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    var nama by remember { mutableStateOf("") }
    var bulan by remember { mutableStateOf("") }
    var noKamar by remember { mutableStateOf("") }
    var totalTagihan by remember { mutableStateOf("") }

    // 🔹 State untuk gambar dan dialog
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 🔹 Scroll State
    val scrollState = rememberScrollState()

    // 🔹 Launcher untuk galeri
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    // 🔹 Launcher untuk kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0D5))
    ) {
        TopAppBar(
            title = { Text("Lakukan Pembayaran") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFAED6D3)
            )
        )

        // 🔹 Bagian scrollable
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 🔹 TextFields
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFDCE8DC),
                    focusedContainerColor = Color(0xFFDCE8DC)
                )
            )
            OutlinedTextField(
                value = bulan,
                onValueChange = { bulan = it },
                label = { Text("Tagihan Bulan berapa") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFDCE8DC),
                    focusedContainerColor = Color(0xFFDCE8DC)
                )
            )
            OutlinedTextField(
                value = noKamar,
                onValueChange = { noKamar = it },
                label = { Text("No. Kamar") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFDCE8DC),
                    focusedContainerColor = Color(0xFFDCE8DC)
                )
            )
            OutlinedTextField(
                value = totalTagihan,
                onValueChange = { totalTagihan = it },
                label = { Text("Total Tagihan") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFDCE8DC),
                    focusedContainerColor = Color(0xFFDCE8DC)
                )
            )

            // 🔹 Metode Pembayaran
            Text("Metode Pembayaran", style = MaterialTheme.typography.titleMedium)
            RekeningCard(bank = "BCA", nomor = "70055792666", nama = "Asrama", logo = R.drawable.ic_bca)
            RekeningCard(bank = "BNI", nomor = "18005579266", nama = "Asrama", logo = R.drawable.ic_bni)

            // 🔹 Upload bukti pembayaran
            Text("Upload Bukti Pembayaran", style = MaterialTheme.typography.titleMedium)

            OutlinedButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Upload",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pilih Foto / Kamera")
            }

            // 🔹 Tampilkan gambar yang dipilih
            selectedImageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Bukti Pembayaran",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Tombol Kirim & Batal
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onSubmitClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E6664))
                ) {
                    Text("Kirim")
                }
                Button(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E6664))
                ) {
                    Text("Batal")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 🔹 Dialog Pilihan Upload
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(220.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Pilih Metode Upload", style = MaterialTheme.typography.titleMedium)
                    Button(
                        onClick = {
                            showDialog = false
                            pickImageLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("📁 Pilih Foto") }
                    Button(
                        onClick = {
                            showDialog = false
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraLauncher.launch(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("📸 Ambil Foto") }
                }
            }
        }
    }
}

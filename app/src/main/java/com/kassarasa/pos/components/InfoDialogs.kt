package com.kassarasa.pos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.model.ManagedMenuItem
import com.kassarasa.pos.model.TransactionRecord
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.CreamDashboardBg
import com.kassarasa.pos.theme.EspressoDark
import com.kassarasa.pos.theme.OliveDark
import com.kassarasa.pos.theme.OliveHeader
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusGreenText
import com.kassarasa.pos.theme.StatusRedText
import com.kassarasa.pos.theme.StatusSuccessBg
import com.kassarasa.pos.theme.StatusSuccessText
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm

@Composable
fun InfoSectionDialog(
    isFitur: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamDashboardBg,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = OliveDark,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = if (isFitur) "Fitur Unggulan KassaRasa" else "Tentang KassaRasa",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        },
        text = {
            if (isFitur) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FeatureLine("• Dashboard Penjualan Harian & Grafik 7 Hari")
                    FeatureLine("• Kelola Menu, Harga, Kategori & Stok Real-Time")
                    FeatureLine("• Kasir POS Cepat dengan Ringkasan Pesanan")
                    FeatureLine("• Pembayaran Tunai, Transfer Bank & QRIS")
                    FeatureLine("• Cetak Preview Struk & Riwayat Transaksi")
                }
            } else {
                Text(
                    text = "KassaRasa adalah sistem kasir digital terpadu untuk UMKM kuliner dan kafe. Dirancang agar pencatatan pesanan, pengelolaan stok menu, pembayaran, hingga laporan penjualan harian berjalan cepat dan rapi.",
                    fontSize = 13.5.sp,
                    color = TextSecondaryWarm,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OliveDark),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Tutup", color = Color.White)
            }
        }
    )
}

@Composable
private fun FeatureLine(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        color = TextPrimaryDark,
        lineHeight = 18.sp
    )
}

@Composable
fun ProfileAccountDialog(
    userName: String,
    userEmail: String,
    isShiftOpen: Boolean,
    onToggleShift: () -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamDashboardBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Akun Kasir",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimaryDark
                )
                Text(
                    text = userEmail,
                    fontSize = 13.sp,
                    color = TextSecondaryWarm
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Status Shift:",
                        fontSize = 13.sp,
                        color = TextSecondaryWarm
                    )
                    Text(
                        text = if (isShiftOpen) "Kasir Aktif" else "Kasir Tutup",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isShiftOpen) StatusGreenText else StatusRedText
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EspressoDark),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Keluar Akun", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onToggleShift()
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (isShiftOpen) "Tutup Kasir" else "Buka Kasir",
                    color = TextPrimaryDark
                )
            }
        }
    )
}

@Composable
fun CloseShiftSummaryDialog(
    isShiftOpen: Boolean,
    totalSales: Int,
    totalOrders: Int,
    onConfirmToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamDashboardBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = if (isShiftOpen) "Tutup Shift Kasir?" else "Buka Shift Kasir?",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isShiftOpen) {
                        "Rekap penjualan hari ini sebelum menutup kasir:"
                    } else {
                        "Kasir saat ini berstatus tutup. Aktifkan kembali untuk mencatat transaksi baru."
                    },
                    fontSize = 13.sp,
                    color = TextSecondaryWarm
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurfaceWhite)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Penjualan", fontSize = 11.5.sp, color = TextMutedWarm)
                        Text(formatRupiah(totalSales), fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Total Pesanan", fontSize = 11.5.sp, color = TextMutedWarm)
                        Text("$totalOrders pesanan", fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmToggle()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = OliveDark),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (isShiftOpen) "Tutup Kasir" else "Aktifkan Kasir", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TextPrimaryDark)
            }
        }
    )
}

@Composable
fun ReceiptDetailDialog(
    record: TransactionRecord,
    onPrintNotice: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardSurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(OliveHeader),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("K", color = Color.White, fontFamily = SerifHeading, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text(
                        text = "Kassa Rassa",
                        fontFamily = SerifHeading,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimaryDark
                    )
                }
                Text(
                    text = "Jl. Rassa Kuliner No. 12, Bandung",
                    fontSize = 11.sp,
                    color = TextMutedWarm
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DashedDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(record.id, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryDark)
                    Text("${record.dateDisplay} • ${record.timeDisplay}", fontSize = 11.sp, color = TextMutedWarm)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Meja: ${record.tableName}", fontSize = 11.5.sp, color = TextSecondaryWarm)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusSuccessBg)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(record.status, fontSize = 10.5.sp, color = StatusSuccessText, fontWeight = FontWeight.SemiBold)
                    }
                }
                DashedDivider()
                record.items.forEach { line ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(line.name, fontSize = 12.5.sp, fontWeight = FontWeight.Medium, color = TextPrimaryDark)
                            Text("${line.quantity}x ${formatRupiah(line.unitPrice)}", fontSize = 11.sp, color = TextMutedWarm)
                        }
                        Text(formatRupiah(line.lineTotal), fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryDark)
                    }
                }
                DashedDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Metode (${record.method.title})", fontSize = 12.sp, color = TextSecondaryWarm)
                    Text(formatRupiah(record.paidAmount), fontSize = 12.sp, color = TextPrimaryDark)
                }
                if (record.changeAmount > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Kembalian", fontSize = 12.sp, color = TextSecondaryWarm)
                        Text(formatRupiah(record.changeAmount), fontSize = 12.sp, color = TextPrimaryDark)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Akhir", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    Text(formatRupiah(record.totalAmount), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                }
                Spacer(modifier = Modifier.height(4.dp))
                BarcodeGraphic(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                Text(
                    text = "Terima Kasih Atas Kunjungan Anda",
                    fontSize = 10.5.sp,
                    color = TextMutedWarm,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OliveHeader),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Tutup", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onPrintNotice,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Outlined.Print, contentDescription = null, tint = TextPrimaryDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cetak", color = TextPrimaryDark)
            }
        }
    )
}

@Composable
fun PaymentSuccessDialog(
    record: TransactionRecord,
    onGoHistory: () -> Unit,
    onNewTransaction: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onNewTransaction,
        containerColor = CreamDashboardBg,
        shape = RoundedCornerShape(22.dp),
        icon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = StatusSuccessText,
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = "Pembayaran Berhasil!",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Transaksi ${record.id} telah dicatat.",
                    fontSize = 13.sp,
                    color = TextSecondaryWarm,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = formatRupiah(record.totalAmount),
                    fontFamily = SerifHeading,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = OliveHeader
                )
                if (record.changeAmount > 0) {
                    Text(
                        text = "Kembalian: ${formatRupiah(record.changeAmount)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onNewTransaction,
                colors = ButtonDefaults.buttonColors(containerColor = OliveHeader),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Transaksi Baru", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onGoHistory,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Lihat Riwayat", color = TextPrimaryDark)
            }
        }
    )
}

@Composable
fun StockAlertsDialog(
    menus: List<ManagedMenuItem>,
    onDismiss: () -> Unit
) {
    val lowOrEmpty = menus.filter { !it.isAvailable || it.stock <= 15 }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamDashboardBg,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = null,
                tint = EspressoDark,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = "Notifikasi Stok Menu",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        },
        text = {
            if (lowOrEmpty.isEmpty()) {
                Text("Semua stok menu aman dan tersedia.", color = TextSecondaryWarm, fontSize = 13.5.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    lowOrEmpty.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CardSurfaceWhite)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryDark)
                            Text(
                                text = if (!item.isAvailable || item.stock == 0) "Habis" else "Sisa ${item.stock} ${item.unit}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (!item.isAvailable || item.stock == 0) StatusRedText else EspressoDark
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OliveDark),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Mengerti", color = Color.White)
            }
        }
    )
}

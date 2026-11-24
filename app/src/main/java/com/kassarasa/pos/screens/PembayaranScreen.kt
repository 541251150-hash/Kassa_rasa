package com.kassarasa.pos.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.components.BarcodeGraphic
import com.kassarasa.pos.components.DashedDivider
import com.kassarasa.pos.components.PaymentSuccessDialog
import com.kassarasa.pos.components.QrisMatrixGraphic
import com.kassarasa.pos.components.ReceiptDetailDialog
import com.kassarasa.pos.model.PaymentMethod
import com.kassarasa.pos.model.ReceiptLineItem
import com.kassarasa.pos.model.TransactionRecord
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.BorderCardSubtle
import com.kassarasa.pos.theme.BorderInput
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.CreamPaymentBg
import com.kassarasa.pos.theme.DividerSoft
import com.kassarasa.pos.theme.OliveHeader
import com.kassarasa.pos.theme.OliveHeaderSub
import com.kassarasa.pos.theme.PaymentChangeBg
import com.kassarasa.pos.theme.PaymentFooterBg
import com.kassarasa.pos.theme.PaymentSelectedBg
import com.kassarasa.pos.theme.PaymentTotalBox
import com.kassarasa.pos.theme.QrisRedText
import com.kassarasa.pos.theme.QuickCashSage
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusActiveDot
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PembayaranScreen(
    viewModel: KassaRasaViewModel,
    onNavigate: (AppRoute) -> Unit,
    onBack: () -> Unit
) {
    var completedTransaction by remember { mutableStateOf<TransactionRecord?>(null) }
    var showPrintPreview by remember { mutableStateOf(false) }
    var showPrintedToastDialog by remember { mutableStateOf(false) }
    var showCustomCashInput by remember { mutableStateOf(false) }

    val totalAmount = viewModel.cartTotalAmount
    val paidAmount = if (viewModel.selectedPaymentMethod == PaymentMethod.CASH) {
        viewModel.cashReceivedAmount
    } else {
        totalAmount
    }
    val changeAmount = (paidAmount - totalAmount).coerceAtLeast(0)
    val nowFormatted = remember {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    }

    var customCashText by remember(viewModel.cashReceivedAmount) {
        mutableStateOf(if (viewModel.cashReceivedAmount > 0) viewModel.cashReceivedAmount.toString() else "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamPaymentBg)
    ) {
        // Two-tier Olive Header matching Halaman Pembayaran.png
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(OliveHeader)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEAE0D0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "KR",
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(
                            text = "Kassa Rassa",
                            fontFamily = SerifHeading,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Point of Sale System",
                            fontSize = 10.5.sp,
                            color = Color(0xFFC8CCB6)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Beranda",
                        color = Color(0xFFEAECE0),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onNavigate(AppRoute.Dashboard) }
                    )
                    Text(
                        text = "|",
                        color = Color(0xFF7B7E5E),
                        fontSize = 12.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF5D613C))
                            .clickable { onNavigate(AppRoute.Transaksi) }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Kasir",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Darker Olive Sub-Nav Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OliveHeaderSub)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubTabItem(label = "Beranda", active = false) { onNavigate(AppRoute.Dashboard) }
                SubTabItem(label = "Kelola Menu", active = false) { onNavigate(AppRoute.KelolaMenu) }
                SubTabItem(label = "Pembayaran", active = true) {}
                SubTabItem(label = "Riwayat", active = false) { onNavigate(AppRoute.Riwayat) }
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Title Row with Back Arrow
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Kembali",
                    tint = TextSecondaryWarm,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable(onClick = onBack)
                )
                Column {
                    Text(
                        text = "Pembayaran",
                        fontFamily = SerifHeading,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "Selesaikan pembayaran untuk memproses pesanan.",
                        fontSize = 12.sp,
                        color = TextMutedWarm
                    )
                }
            }

            if (viewModel.cartItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(CardSurfaceWhite)
                        .border(1.dp, BorderCardSubtle, RoundedCornerShape(18.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Belum ada item di keranjang.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "Tambahkan pesanan dari halaman Kasir / Transaksi terlebih dahulu.",
                        fontSize = 12.5.sp,
                        color = TextMutedWarm,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { onNavigate(AppRoute.Transaksi) },
                        colors = ButtonDefaults.buttonColors(containerColor = OliveHeader),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Pilih Menu di Kasir", color = Color.White)
                    }
                }
            } else {
                // Total Pembayaran Banner Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(PaymentTotalBox)
                        .border(1.dp, Color(0xFFE4D2BC), RoundedCornerShape(16.dp))
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL PEMBAYARAN",
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF7A6A58)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = formatRupiah(totalAmount),
                        fontFamily = SerifHeading,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color(0xFF2D2319)
                    )
                }

                // Payment Method Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFFFFDF9))
                        .border(1.dp, BorderCardSubtle, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "METODE PEMBAYARAN",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = Color(0xFF6D6256)
                    )

                    PaymentMethod.entries.forEach { method ->
                        val selected = viewModel.selectedPaymentMethod == method
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) PaymentSelectedBg else CardSurfaceWhite)
                                .border(
                                    width = if (selected) 1.5.dp else 1.dp,
                                    color = if (selected) OliveHeader else Color(0xFFE5DDD2),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.selectedPaymentMethod = method
                                    if (method == PaymentMethod.CASH && viewModel.cashReceivedAmount < totalAmount) {
                                        viewModel.cashReceivedAmount = if (totalAmount <= 50_000) 50_000 else totalAmount
                                    }
                                }
                                .padding(horizontal = 14.dp, vertical = 13.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(if (selected) OliveHeader else Color.Transparent)
                                        .border(
                                            width = if (selected) 0.dp else 1.5.dp,
                                            color = Color(0xFFC5BEB5),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color.White)
                                        )
                                    }
                                }

                                Text(
                                    text = method.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = TextPrimaryDark
                                )
                            }

                            when (method) {
                                PaymentMethod.CASH -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(OliveHeader)
                                            .padding(horizontal = 9.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "CASH",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                PaymentMethod.BANK_TRANSFER -> {
                                    Text(
                                        text = "BCA / Mandiri",
                                        fontSize = 11.5.sp,
                                        color = TextMutedWarm
                                    )
                                }
                                PaymentMethod.QRIS -> {
                                    Text(
                                        text = "QRIS",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QrisRedText
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        color = DividerSoft,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    when (viewModel.selectedPaymentMethod) {
                        PaymentMethod.CASH -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Nominal Cepat:",
                                    fontSize = 12.sp,
                                    color = TextSecondaryWarm
                                )
                                Text(
                                    text = if (showCustomCashInput) "Tutup Input" else "Input Nominal Lain",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OliveHeader,
                                    modifier = Modifier.clickable { showCustomCashInput = !showCustomCashInput }
                                )
                            }

                            val exactSelected = viewModel.cashReceivedAmount == totalAmount
                            val fiftySelected = viewModel.cashReceivedAmount == 50_000
                            val hundredSelected = viewModel.cashReceivedAmount == 100_000

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickCashChip(
                                    label = "Uang Pas",
                                    selected = exactSelected,
                                    onClick = { viewModel.cashReceivedAmount = totalAmount },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickCashChip(
                                    label = "Rp 50.000",
                                    selected = fiftySelected,
                                    onClick = { viewModel.cashReceivedAmount = 50_000 },
                                    modifier = Modifier.weight(1f)
                                )
                                if (totalAmount > 50_000) {
                                    QuickCashChip(
                                        label = "Rp 100.000",
                                        selected = hundredSelected,
                                        onClick = { viewModel.cashReceivedAmount = 100_000 },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            if (showCustomCashInput) {
                                OutlinedTextField(
                                    value = customCashText,
                                    onValueChange = { input ->
                                        val digits = input.filter { it.isDigit() }
                                        customCashText = digits
                                        viewModel.cashReceivedAmount = digits.toIntOrNull() ?: 0
                                    },
                                    label = { Text("Nominal Diterima (Rp)", fontSize = 12.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = CardSurfaceWhite,
                                        unfocusedContainerColor = CardSurfaceWhite,
                                        focusedBorderColor = OliveHeader,
                                        unfocusedBorderColor = BorderInput
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PaymentChangeBg)
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Kembalian:",
                                    fontSize = 12.5.sp,
                                    color = TextSecondaryWarm
                                )
                                Text(
                                    text = formatRupiah(changeAmount),
                                    fontFamily = SerifHeading,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF2D2319)
                                )
                            }
                        }

                        PaymentMethod.BANK_TRANSFER -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PaymentChangeBg)
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Rekening Tujuan Transfer",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimaryDark
                                )
                                Text("BCA: 2810-4459-12 a.n. KassaRasa Kuliner", fontSize = 12.sp, color = TextSecondaryWarm)
                                Text("Mandiri: 1300-0912-8871 a.n. KassaRasa Kuliner", fontSize = 12.sp, color = TextSecondaryWarm)
                            }
                        }

                        PaymentMethod.QRIS -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PaymentChangeBg)
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Scan QRIS • Kassa Rassa Bandung",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimaryDark
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .padding(10.dp)
                                ) {
                                    QrisMatrixGraphic()
                                }
                                Text(
                                    text = "Nominal otomatis terverifikasi: ${formatRupiah(totalAmount)}",
                                    fontSize = 11.5.sp,
                                    color = TextSecondaryWarm
                                )
                            }
                        }
                    }
                }

                // Live Receipt Preview Card (Struk) matching Halaman Pembayaran.png
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(CardSurfaceWhite)
                        .border(1.dp, BorderCardSubtle, RoundedCornerShape(18.dp))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(OliveHeader),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "K",
                                    color = Color.White,
                                    fontFamily = SerifHeading,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            }
                            Text(
                                text = "Kassa Rassa",
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimaryDark
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Jl. Rassa Kuliner No. 12, Bandung",
                            fontSize = 11.sp,
                            color = TextMutedWarm
                        )
                    }

                    DashedDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "No. ${viewModel.lastReceiptNumber}",
                            fontSize = 11.sp,
                            color = TextSecondaryWarm
                        )
                        Text(
                            text = nowFormatted,
                            fontSize = 11.sp,
                            color = TextSecondaryWarm
                        )
                    }

                    DashedDivider()

                    viewModel.cartItems.forEach { cartItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.product.name,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "${cartItem.quantity}x ${formatRupiah(cartItem.product.price)}",
                                    fontSize = 11.sp,
                                    color = TextMutedWarm
                                )
                            }
                            Text(
                                text = formatRupiah(cartItem.subtotal),
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimaryDark
                            )
                        }
                    }

                    DashedDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 12.sp, color = TextSecondaryWarm)
                        Text(formatRupiah(totalAmount), fontSize = 12.sp, color = TextPrimaryDark)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Pajak (0%)", fontSize = 12.sp, color = TextSecondaryWarm)
                        Text("Rp 0", fontSize = 12.sp, color = TextPrimaryDark)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Akhir", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Text(formatRupiah(totalAmount), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }

                    DashedDivider()

                    BarcodeGraphic(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                    )
                    Text(
                        text = "Terima Kasih Atas Kunjungan Anda",
                        fontSize = 10.5.sp,
                        color = TextMutedWarm,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Fixed Bottom Bar matching Halaman Pembayaran.png
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PaymentFooterBg)
                .navigationBarsPadding()
        ) {
            HorizontalDivider(color = Color(0xFFE5DACB), thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showPrintPreview = true },
                    enabled = viewModel.cartItems.isNotEmpty(),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = CardSurfaceWhite),
                    border = BorderStroke(1.dp, OliveHeader),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Print,
                        contentDescription = null,
                        tint = TextPrimaryDark,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cetak Struk",
                        color = TextPrimaryDark,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {
                        val record = viewModel.completePayment()
                        if (record != null) {
                            completedTransaction = record
                        }
                    },
                    enabled = viewModel.cartItems.isNotEmpty() &&
                        (viewModel.selectedPaymentMethod != PaymentMethod.CASH || viewModel.cashReceivedAmount >= totalAmount),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OliveHeader,
                        disabledContainerColor = Color(0xFFB7B3A8)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Selesai / Bayar",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }

    if (showPrintPreview && viewModel.cartItems.isNotEmpty()) {
        val previewRecord = TransactionRecord(
            id = viewModel.lastReceiptNumber,
            status = "Preview",
            dateDisplay = nowFormatted.substringBefore(" "),
            dateGroup = "Hari Ini",
            timeDisplay = nowFormatted.substringAfter(" ") + " WIB",
            method = viewModel.selectedPaymentMethod,
            totalAmount = totalAmount,
            tableName = viewModel.selectedTable,
            paidAmount = paidAmount,
            changeAmount = changeAmount,
            items = viewModel.cartItems.map {
                ReceiptLineItem(it.product.name, it.quantity, it.product.price)
            }
        )
        ReceiptDetailDialog(
            record = previewRecord,
            onPrintNotice = {
                showPrintPreview = false
                showPrintedToastDialog = true
            },
            onDismiss = { showPrintPreview = false }
        )
    }

    if (showPrintedToastDialog) {
        AlertDialog(
            onDismissRequest = { showPrintedToastDialog = false },
            containerColor = CreamPaymentBg,
            shape = RoundedCornerShape(18.dp),
            title = {
                Text("Struk Siap Dicetak", fontFamily = SerifHeading, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
            },
            text = {
                Text(
                    text = "Simulasi cetak struk dikirim ke printer thermal kasir.",
                    fontSize = 13.sp,
                    color = TextSecondaryWarm
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPrintedToastDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = OliveHeader)
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }

    if (completedTransaction != null) {
        PaymentSuccessDialog(
            record = completedTransaction!!,
            onGoHistory = {
                completedTransaction = null
                onNavigate(AppRoute.Riwayat)
            },
            onNewTransaction = {
                completedTransaction = null
                onNavigate(AppRoute.Transaksi)
            }
        )
    }
}

@Composable
private fun SubTabItem(
    label: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) Color(0xFF4E5132) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (active) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(StatusActiveDot)
            )
        }
        Text(
            text = label,
            color = if (active) Color.White else Color(0xFFD6DAC5),
            fontSize = 12.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
private fun QuickCashChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) QuickCashSage else PaymentTotalBox)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else Color(0xFF3D3329)
        )
    }
}

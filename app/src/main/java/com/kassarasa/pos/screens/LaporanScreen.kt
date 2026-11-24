package com.kassarasa.pos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.components.CashierStatusPill
import com.kassarasa.pos.components.DashboardBottomBar
import com.kassarasa.pos.components.InitialsAvatar
import com.kassarasa.pos.components.KassaRasaLogoImage
import com.kassarasa.pos.components.ProfileAccountDialog
import com.kassarasa.pos.model.PaymentMethod
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.AvatarBeige
import com.kassarasa.pos.theme.BorderWarmLight
import com.kassarasa.pos.theme.CardSurfaceWarm
import com.kassarasa.pos.theme.CreamDashboardBg
import com.kassarasa.pos.theme.OliveDark
import com.kassarasa.pos.theme.QuickCashSage
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusGreenText
import com.kassarasa.pos.theme.TextHeadingBrown
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm

@Composable
fun LaporanScreen(
    viewModel: KassaRasaViewModel,
    onNavigate: (AppRoute) -> Unit,
    onLogout: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Minggu Ini") }
    var showProfileDialog by remember { mutableStateOf(false) }

    val periods = listOf("Hari Ini", "Minggu Ini", "Bulan Ini")
    val periodMultiplier = when (selectedPeriod) {
        "Hari Ini" -> 1
        "Minggu Ini" -> 1
        else -> 4
    }

    val historyTotal = viewModel.transactionHistory.sumOf { it.totalAmount }.coerceAtLeast(1)
    val breakdown = PaymentMethod.entries.map { method ->
        val sum = viewModel.transactionHistory.filter { it.method == method }.sumOf { it.totalAmount }
        val ratio = sum.toFloat() / historyTotal.toFloat()
        Triple(method, sum, ratio)
    }

    val avgOrder = if (viewModel.totalOrdersToday > 0) {
        viewModel.totalSalesToday / viewModel.totalOrdersToday
    } else 0

    Scaffold(
        containerColor = CreamDashboardBg,
        bottomBar = {
            DashboardBottomBar(
                currentRoute = AppRoute.Laporan,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F2EA))
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KassaRasaLogoImage(height = 30.dp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CashierStatusPill(
                            isActive = viewModel.isCashierShiftOpen,
                            onClick = { viewModel.toggleCashierShift() }
                        )
                        InitialsAvatar(
                            initials = viewModel.loggedInUserName.take(2).uppercase().ifBlank { "AD" },
                            backgroundColor = AvatarBeige,
                            onClick = { showProfileDialog = true }
                        )
                    }
                }
                HorizontalDivider(color = BorderWarmLight, thickness = 1.dp)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = "Laporan Penjualan",
                            fontFamily = SerifHeading,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = TextHeadingBrown
                        )
                        Text(
                            text = "Ringkasan performa kasir & metode pembayaran",
                            fontSize = 12.5.sp,
                            color = TextMutedWarm
                        )
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        periods.forEach { period ->
                            val selected = selectedPeriod == period
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (selected) OliveDark else CardSurfaceWarm)
                                    .border(1.dp, if (selected) OliveDark else BorderWarmLight, RoundedCornerShape(50))
                                    .clickable { selectedPeriod = period }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = period,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (selected) Color.White else TextPrimaryDark
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(CardSurfaceWarm)
                                .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Pendapatan ($selectedPeriod)", fontSize = 12.sp, color = TextSecondaryWarm)
                            Text(
                                text = formatRupiah(viewModel.totalSalesToday * periodMultiplier),
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextHeadingBrown
                            )
                            Text("+14.8% vs periode lalu", fontSize = 11.5.sp, color = StatusGreenText)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(CardSurfaceWarm)
                                .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Rata-rata Order", fontSize = 12.sp, color = TextSecondaryWarm)
                            Text(
                                text = formatRupiah(avgOrder),
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextHeadingBrown
                            )
                            Text("${viewModel.totalOrdersToday * periodMultiplier} pesanan", fontSize = 11.5.sp, color = TextMutedWarm)
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(CardSurfaceWarm)
                            .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Rekap Metode Pembayaran",
                            fontFamily = SerifHeading,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextHeadingBrown
                        )

                        breakdown.forEach { (method, sum, ratio) ->
                            val barColor = when (method) {
                                PaymentMethod.CASH -> OliveDark
                                PaymentMethod.QRIS -> QuickCashSage
                                PaymentMethod.BANK_TRANSFER -> Color(0xFF6C5843)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(method.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimaryDark)
                                    Text(
                                        text = "${formatRupiah(sum)} (${(ratio * 100).toInt()}%)",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryDark
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color(0xFFEDE4D8))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(ratio.coerceIn(0.05f, 1f))
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(50))
                                            .background(barColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showProfileDialog) {
        ProfileAccountDialog(
            userName = viewModel.loggedInUserName,
            userEmail = viewModel.loggedInEmail,
            isShiftOpen = viewModel.isCashierShiftOpen,
            onToggleShift = { viewModel.toggleCashierShift() },
            onLogout = {
                viewModel.logout()
                onLogout()
            },
            onDismiss = { showProfileDialog = false }
        )
    }
}

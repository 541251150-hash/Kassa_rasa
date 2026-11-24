package com.kassarasa.pos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.rounded.Add
import java.util.Locale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.components.CashierStatusPill
import com.kassarasa.pos.components.CloseShiftSummaryDialog
import com.kassarasa.pos.components.DashboardBottomBar
import com.kassarasa.pos.components.InitialsAvatar
import com.kassarasa.pos.components.KassaRasaLogoImage
import com.kassarasa.pos.components.ProfileAccountDialog
import com.kassarasa.pos.model.TopMenuSummary
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.AvatarBeige
import com.kassarasa.pos.theme.BorderPill
import com.kassarasa.pos.theme.BorderWarmLight
import com.kassarasa.pos.theme.CardSurfaceWarm
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.ChartBarDark
import com.kassarasa.pos.theme.ChartBarLight
import com.kassarasa.pos.theme.ChartBarMid
import com.kassarasa.pos.theme.CreamDashboardBg
import com.kassarasa.pos.theme.DividerSoft
import com.kassarasa.pos.theme.OliveDark
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusGreenMintBg
import com.kassarasa.pos.theme.StatusGreenText
import com.kassarasa.pos.theme.TextHeadingBrown
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm
import com.kassarasa.pos.theme.ThumbnailBeige

@Composable
fun DashboardScreen(
    viewModel: KassaRasaViewModel,
    onNavigate: (AppRoute) -> Unit,
    onLogout: () -> Unit
) {
    var selectedDayIndex by remember { mutableIntStateOf(6) } // Default "Min" (3.0M)
    var showProfileDialog by remember { mutableStateOf(false) }
    var showCloseShiftDialog by remember { mutableStateOf(false) }

    val initials = viewModel.loggedInUserName
        .take(2)
        .uppercase()
        .ifBlank { "AD" }

    Scaffold(
        containerColor = CreamDashboardBg,
        bottomBar = {
            DashboardBottomBar(
                currentRoute = AppRoute.Dashboard,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Top Bar matching Dashboard.png
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
                            onClick = { showCloseShiftDialog = true }
                        )
                        InitialsAvatar(
                            initials = initials,
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
                // Greeting Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Halo, ${viewModel.loggedInUserName}",
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = TextHeadingBrown
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Pantau aktivitas toko hari ini",
                                fontSize = 12.5.sp,
                                color = TextMutedWarm
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEDE3D6))
                                .border(1.dp, Color(0xFFDFD3C3), RoundedCornerShape(10.dp))
                                .padding(horizontal = 11.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Kamis, 24 Okt",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4A3B2C)
                            )
                        }
                    }
                }

                // KPI Card 1: Total Penjualan
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(CardSurfaceWarm)
                            .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Total Penjualan",
                                fontSize = 13.sp,
                                color = TextSecondaryWarm
                            )
                            Text(
                                text = formatRupiah(viewModel.totalSalesToday),
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = TextHeadingBrown
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.TrendingUp,
                                    contentDescription = null,
                                    tint = StatusGreenText,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "+14.8% vs kemarin",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = StatusGreenText
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF2EAE0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MonetizationOn,
                                contentDescription = null,
                                tint = Color(0xFF4A3D31),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // KPI Row: Total Pesanan & Menu Aktif
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
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Pesanan",
                                    fontSize = 12.5.sp,
                                    color = TextSecondaryWarm
                                )
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(Color(0xFFF2EAE0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Assignment,
                                        contentDescription = null,
                                        tint = Color(0xFF4A3D31),
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${viewModel.totalOrdersToday}",
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = TextHeadingBrown
                            )
                            Text(
                                text = "Selesai diproses",
                                fontSize = 11.5.sp,
                                color = TextMutedWarm
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(CardSurfaceWarm)
                                .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                                .clickable { onNavigate(AppRoute.KelolaMenu) }
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Menu Aktif",
                                    fontSize = 12.5.sp,
                                    color = TextSecondaryWarm
                                )
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(Color(0xFFF2EAE0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.RestaurantMenu,
                                        contentDescription = null,
                                        tint = Color(0xFF4A3D31),
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${viewModel.activeMenuDisplayCount}",
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = TextHeadingBrown
                            )
                            Text(
                                text = "Semua siap saji",
                                fontSize = 11.5.sp,
                                color = StatusGreenText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Weekly Bar Chart Card
                item {
                    val points = viewModel.weeklySalesPoints
                    val maxMillion = 3.1f
                    val barPalette = listOf(
                        ChartBarLight,
                        Color(0xFF604B37),
                        Color(0xFF68533E),
                        ChartBarMid,
                        Color(0xFF443525),
                        ChartBarDark,
                        OliveDark
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(CardSurfaceWarm)
                            .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Grafik penjualan 7 hari terakhir",
                                    fontFamily = SerifHeading,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.5.sp,
                                    color = TextHeadingBrown
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Tren pendapatan mingguan",
                                    fontSize = 11.5.sp,
                                    color = TextMutedWarm
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF2EAE0))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "Minggu Ini",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF5C4D3E)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            points.forEachIndexed { index, point ->
                                val isSelected = index == selectedDayIndex
                                val fraction = (point.amountMillion / maxMillion).coerceIn(0.2f, 1f)
                                val barColor = if (isSelected) OliveDark else barPalette[index]
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedDayIndex = index },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    Text(
                                        text = if (isSelected) "${String.format(Locale.US, "%.1f", point.amountMillion)}M" else "",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextHeadingBrown,
                                        modifier = Modifier.height(16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height((98.dp * fraction))
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(barColor)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = DividerSoft, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            points.forEachIndexed { index, point ->
                                val isSelected = index == selectedDayIndex
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = point.dayLabel,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextHeadingBrown else TextMutedWarm
                                    )
                                }
                            }
                        }
                    }
                }

                // Menu Terlaris Card
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(CardSurfaceWarm)
                            .border(1.dp, BorderWarmLight, RoundedCornerShape(18.dp))
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Menu Terlaris",
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextHeadingBrown
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onNavigate(AppRoute.KelolaMenu) }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Lihat Semua",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF4A3B2C)
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color(0xFF4A3B2C),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        viewModel.topMenus.forEach { item ->
                            TopMenuRowCard(item = item)
                        }
                    }
                }

                // Bottom Action Buttons Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(AppRoute.Transaksi) },
                            colors = ButtonDefaults.buttonColors(containerColor = OliveDark),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 13.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Transaksi Baru",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        OutlinedButton(
                            onClick = { showCloseShiftDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = CardSurfaceWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderPill),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 13.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PointOfSale,
                                contentDescription = null,
                                tint = TextHeadingBrown,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (viewModel.isCashierShiftOpen) "Tutup Kasir" else "Buka Kasir",
                                color = TextHeadingBrown,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
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

    if (showCloseShiftDialog) {
        CloseShiftSummaryDialog(
            isShiftOpen = viewModel.isCashierShiftOpen,
            totalSales = viewModel.totalSalesToday,
            totalOrders = viewModel.totalOrdersToday,
            onConfirmToggle = { viewModel.toggleCashierShift() },
            onDismiss = { showCloseShiftDialog = false }
        )
    }
}

@Composable
private fun TopMenuRowCard(item: TopMenuSummary) {
    val isFirst = item.rank == 1
    val badgeBg = if (isFirst) StatusGreenMintBg else Color(0xFFF1ECE5)
    val badgeText = if (isFirst) StatusGreenText else Color(0xFF6E6358)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFBF7F2))
            .border(1.dp, BorderWarmLight, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ThumbnailBeige)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${formatRupiah(item.unitPrice)} / ${item.unitLabel}",
                fontSize = 11.5.sp,
                color = TextMutedWarm
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${item.soldCount} ${item.unitLabel}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(badgeBg)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Terlaris #${item.rank}",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = badgeText
                )
            }
        }
    }
}

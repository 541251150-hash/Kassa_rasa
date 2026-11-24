package com.kassarasa.pos.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.components.InfoSectionDialog
import com.kassarasa.pos.components.KassaRasaLogoImage
import com.kassarasa.pos.components.ReceiptDetailDialog
import com.kassarasa.pos.components.RiwayatBottomBar
import com.kassarasa.pos.model.PaymentMethod
import com.kassarasa.pos.model.TransactionRecord
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.CreamHeaderBg
import com.kassarasa.pos.theme.CreamHistoryBg
import com.kassarasa.pos.theme.CreamStripBg
import com.kassarasa.pos.theme.OlivePosBar
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusSuccessBg
import com.kassarasa.pos.theme.StatusSuccessBorder
import com.kassarasa.pos.theme.StatusSuccessText
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPlaceholder
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm
import kotlin.math.ceil

@Composable
fun RiwayatTransaksiScreen(
    viewModel: KassaRasaViewModel,
    onNavigate: (AppRoute) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDateFilter by remember { mutableStateOf("Pilih Tanggal") }
    var selectedMethodFilter by remember { mutableStateOf<PaymentMethod?>(null) }
    var currentPage by remember { mutableIntStateOf(1) }

    var showDateDropdown by remember { mutableStateOf(false) }
    var showMethodDropdown by remember { mutableStateOf(false) }
    var selectedDetailRecord by remember { mutableStateOf<TransactionRecord?>(null) }
    var showPrintConfirmation by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf<Boolean?>(null) }

    val dateOptions = listOf("Pilih Tanggal", "Hari Ini", "14 Okt 2023", "13 Okt 2023", "12 Okt 2023")

    val filteredRecords = remember(
        viewModel.transactionHistory.toList(),
        searchQuery,
        selectedDateFilter,
        selectedMethodFilter
    ) {
        viewModel.transactionHistory.filter { rec ->
            val matchSearch = searchQuery.isBlank() ||
                rec.id.contains(searchQuery, ignoreCase = true) ||
                rec.method.title.contains(searchQuery, ignoreCase = true)
            val matchDate = selectedDateFilter == "Pilih Tanggal" || rec.dateGroup == selectedDateFilter
            val matchMethod = selectedMethodFilter == null || rec.method == selectedMethodFilter
            matchSearch && matchDate && matchMethod
        }
    }

    val pageSize = 4
    val totalPages = ceil(filteredRecords.size.coerceAtLeast(1) / pageSize.toDouble()).toInt()
    val safePage = currentPage.coerceIn(1, totalPages)
    val pageItems = filteredRecords.drop((safePage - 1) * pageSize).take(pageSize)

    Scaffold(
        containerColor = CreamHistoryBg,
        bottomBar = {
            RiwayatBottomBar(
                currentRoute = AppRoute.Riwayat,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Top Bar matching Riwayat Transaksi.png
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CreamHeaderBg)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KassaRasaLogoImage(height = 28.dp)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Beranda",
                            fontSize = 13.sp,
                            color = TextSecondaryWarm,
                            modifier = Modifier.clickable { onNavigate(AppRoute.Dashboard) }
                        )
                        Text(
                            text = "Fitur",
                            fontSize = 13.sp,
                            color = TextSecondaryWarm,
                            modifier = Modifier.clickable { showInfoDialog = true }
                        )
                        Text(
                            text = "Tentang",
                            fontSize = 13.sp,
                            color = TextSecondaryWarm,
                            modifier = Modifier.clickable { showInfoDialog = false }
                        )
                    }
                }

                // Sub-Navigation Tab Strip
                HorizontalDivider(color = Color(0xFFEAE0CF), thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CreamStripBg)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StripNavChip(
                        icon = Icons.Outlined.GridView,
                        label = "Dashboard",
                        selected = false,
                        onClick = { onNavigate(AppRoute.Dashboard) }
                    )
                    StripNavChip(
                        icon = Icons.AutoMirrored.Outlined.MenuBook,
                        label = "Kelola Menu",
                        selected = false,
                        onClick = { onNavigate(AppRoute.KelolaMenu) }
                    )
                    StripNavChip(
                        icon = Icons.AutoMirrored.Outlined.ReceiptLong,
                        label = "Transaksi",
                        selected = false,
                        onClick = { onNavigate(AppRoute.Transaksi) }
                    )
                    StripNavChip(
                        icon = Icons.Outlined.History,
                        label = "Riwayat",
                        selected = true,
                        onClick = {}
                    )
                }
                HorizontalDivider(color = Color(0xFFEAE0CF), thickness = 1.dp)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Title
                item {
                    Column {
                        Text(
                            text = "Riwayat Transaksi",
                            fontFamily = SerifHeading,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                            color = TextPrimaryDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Daftar rekapan seluruh transaksi penjualan kasir Kassa Rassa.",
                            fontSize = 12.5.sp,
                            color = TextMutedWarm
                        )
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            currentPage = 1
                        },
                        placeholder = {
                            Text(
                                text = "Cari no. transaksi...",
                                color = TextPlaceholder,
                                fontSize = 13.5.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = TextMutedWarm,
                                modifier = Modifier.size(19.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSurfaceWhite,
                            unfocusedContainerColor = CardSurfaceWhite,
                            focusedBorderColor = OlivePosBar,
                            unfocusedBorderColor = Color(0xFFE8E0D4),
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Date Picker + Filter Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CardSurfaceWhite)
                                    .border(1.dp, Color(0xFFE8E0D4), RoundedCornerShape(12.dp))
                                    .clickable { showDateDropdown = true }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarToday,
                                        contentDescription = null,
                                        tint = TextMutedWarm,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = selectedDateFilter,
                                        fontSize = 13.sp,
                                        color = TextPrimaryDark
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextMutedWarm,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showDateDropdown,
                                onDismissRequest = { showDateDropdown = false },
                                containerColor = CardSurfaceWhite
                            ) {
                                dateOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = if (option == "Pilih Tanggal") "Semua Tanggal" else option,
                                                color = TextPrimaryDark,
                                                fontSize = 13.sp
                                            )
                                        },
                                        onClick = {
                                            selectedDateFilter = option
                                            currentPage = 1
                                            showDateDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selectedMethodFilter != null) OlivePosBar else Color(0xFFEDE3D3))
                                    .clickable { showMethodDropdown = true }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    contentDescription = null,
                                    tint = if (selectedMethodFilter != null) Color.White else TextPrimaryDark,
                                    modifier = Modifier.size(17.dp)
                                )
                                Text(
                                    text = selectedMethodFilter?.title ?: "Filter",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (selectedMethodFilter != null) Color.White else TextPrimaryDark
                                )
                            }

                            DropdownMenu(
                                expanded = showMethodDropdown,
                                onDismissRequest = { showMethodDropdown = false },
                                containerColor = CardSurfaceWhite
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Semua Metode", color = TextPrimaryDark, fontSize = 13.sp) },
                                    onClick = {
                                        selectedMethodFilter = null
                                        currentPage = 1
                                        showMethodDropdown = false
                                    }
                                )
                                PaymentMethod.entries.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m.title, color = TextPrimaryDark, fontSize = 13.sp) },
                                        onClick = {
                                            selectedMethodFilter = m
                                            currentPage = 1
                                            showMethodDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Transaction Cards
                items(pageItems, key = { it.id }) { record ->
                    TransactionHistoryCard(
                        record = record,
                        onInspect = { selectedDetailRecord = record }
                    )
                }

                if (pageItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardSurfaceWhite)
                                .border(1.dp, Color(0xFFEAE3D7), RoundedCornerShape(16.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada transaksi yang sesuai filter.",
                                color = TextMutedWarm,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Pagination Footer matching Riwayat Transaksi.png
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Menampilkan ${pageItems.size} dari ${filteredRecords.size} data",
                            fontSize = 12.sp,
                            color = TextMutedWarm
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PaginationButton(
                                enabled = safePage > 1,
                                active = false,
                                onClick = { if (safePage > 1) currentPage = safePage - 1 }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                    contentDescription = "Sebelumnya",
                                    tint = if (safePage > 1) TextPrimaryDark else Color(0xFFB5ADA3),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            for (page in 1..totalPages) {
                                PaginationButton(
                                    enabled = true,
                                    active = page == safePage,
                                    onClick = { currentPage = page }
                                ) {
                                    Text(
                                        text = "$page",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (page == safePage) Color.White else TextPrimaryDark
                                    )
                                }
                            }

                            PaginationButton(
                                enabled = safePage < totalPages,
                                active = false,
                                onClick = { if (safePage < totalPages) currentPage = safePage + 1 }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = "Berikutnya",
                                    tint = if (safePage < totalPages) TextPrimaryDark else Color(0xFFB5ADA3),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedDetailRecord != null) {
        ReceiptDetailDialog(
            record = selectedDetailRecord!!,
            onPrintNotice = {
                selectedDetailRecord = null
                showPrintConfirmation = true
            },
            onDismiss = { selectedDetailRecord = null }
        )
    }

    if (showPrintConfirmation) {
        AlertDialog(
            onDismissRequest = { showPrintConfirmation = false },
            containerColor = CreamHistoryBg,
            shape = RoundedCornerShape(18.dp),
            title = { Text("Cetak Ulang Struk", fontFamily = SerifHeading, fontWeight = FontWeight.Bold) },
            text = { Text("Simulasi cetak ulang struk berhasil dikirim ke printer kasir.", fontSize = 13.sp, color = TextSecondaryWarm) },
            confirmButton = {
                Button(
                    onClick = { showPrintConfirmation = false },
                    colors = ButtonDefaults.buttonColors(containerColor = OlivePosBar)
                ) {
                    Text("Tutup", color = Color.White)
                }
            }
        )
    }

    if (showInfoDialog != null) {
        InfoSectionDialog(
            isFitur = showInfoDialog == true,
            onDismiss = { showInfoDialog = null }
        )
    }
}

@Composable
private fun StripNavChip(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) OlivePosBar else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) Color.White else TextPrimaryDark,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) Color.White else TextPrimaryDark
        )
    }
}

@Composable
private fun TransactionHistoryCard(
    record: TransactionRecord,
    onInspect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurfaceWhite)
            .border(1.dp, Color(0xFFEAE3D7), RoundedCornerShape(16.dp))
            .clickable(onClick = onInspect)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = record.id,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(StatusSuccessBg)
                        .border(1.dp, StatusSuccessBorder, RoundedCornerShape(50))
                        .padding(horizontal = 9.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = record.status,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StatusSuccessText
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "Detail ${record.id}",
                tint = TextSecondaryWarm,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onInspect)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "${record.dateDisplay} \u2022 ${record.timeDisplay}",
                    fontSize = 11.5.sp,
                    color = TextMutedWarm
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Metode:",
                        fontSize = 12.sp,
                        color = TextSecondaryWarm
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF3EDE3))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = record.method.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B433A)
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Total",
                    fontSize = 11.sp,
                    color = TextMutedWarm
                )
                Text(
                    text = formatRupiah(record.totalAmount),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }
        }
    }
}

@Composable
private fun PaginationButton(
    enabled: Boolean,
    active: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) OlivePosBar else Color(0xFFEFE9DD))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

package com.kassarasa.pos.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.components.DeleteMenuConfirmDialog
import com.kassarasa.pos.components.InitialsAvatar
import com.kassarasa.pos.components.KassaRasaLogoImage
import com.kassarasa.pos.components.KelolaMenuBottomBar
import com.kassarasa.pos.components.MenuFormDialog
import com.kassarasa.pos.components.ProfileAccountDialog
import com.kassarasa.pos.components.StockAlertsDialog
import com.kassarasa.pos.model.ManagedMenuItem
import com.kassarasa.pos.model.MenuCategory
import com.kassarasa.pos.model.MenuSortOrder
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.AvatarNeutral
import com.kassarasa.pos.theme.BorderCardSubtle
import com.kassarasa.pos.theme.BorderInput
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.CreamMenuBg
import com.kassarasa.pos.theme.OliveNavBar
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusGreenMintBg
import com.kassarasa.pos.theme.StatusGreenMintBorder
import com.kassarasa.pos.theme.StatusGreenText
import com.kassarasa.pos.theme.StatusRedBg
import com.kassarasa.pos.theme.StatusRedBorder
import com.kassarasa.pos.theme.StatusRedText
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPlaceholder
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm

@Composable
fun KelolaMenuScreen(
    viewModel: KassaRasaViewModel,
    onNavigate: (AppRoute) -> Unit,
    onLogout: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(MenuCategory.ALL) }
    var sortOrder by remember { mutableStateOf(MenuSortOrder.NEWEST) }
    var onlyAvailableFilter by remember { mutableStateOf(false) }

    var showAddOrEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ManagedMenuItem?>(null) }
    var deletingItem by remember { mutableStateOf<ManagedMenuItem?>(null) }
    var showAlertsDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val categories = listOf(
        MenuCategory.ALL,
        MenuCategory.FOOD,
        MenuCategory.DRINK,
        MenuCategory.SNACK
    )

    val filteredMenus = remember(
        viewModel.managedMenus.toList(),
        searchQuery,
        selectedCategory,
        sortOrder,
        onlyAvailableFilter
    ) {
        viewModel.managedMenus
            .filter { item ->
                (selectedCategory == MenuCategory.ALL || item.category == selectedCategory) &&
                    (searchQuery.isBlank() || item.name.contains(searchQuery, ignoreCase = true)) &&
                    (!onlyAvailableFilter || item.isAvailable)
            }
            .let { list ->
                when (sortOrder) {
                    MenuSortOrder.NEWEST -> list
                    MenuSortOrder.PRICE_LOW -> list.sortedBy { it.price }
                    MenuSortOrder.PRICE_HIGH -> list.sortedByDescending { it.price }
                    MenuSortOrder.NAME_ASC -> list.sortedBy { it.name }
                }
            }
    }

    Scaffold(
        containerColor = CreamMenuBg,
        bottomBar = {
            KelolaMenuBottomBar(
                currentRoute = AppRoute.KelolaMenu,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Top Bar matching Kelola Menu.png
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CreamMenuBg)
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CardSurfaceWhite)
                                .border(1.dp, BorderInput, CircleShape)
                                .clickable { showAlertsDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifikasi",
                                tint = TextPrimaryDark,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        InitialsAvatar(
                            initials = "KR",
                            backgroundColor = AvatarNeutral,
                            borderColor = Color(0xFFCEC6B9),
                            onClick = { showProfileDialog = true }
                        )
                    }
                }
                HorizontalDivider(color = BorderCardSubtle, thickness = 1.dp)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header + "+ Tambah Menu" button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Kelola Menu",
                                fontFamily = SerifHeading,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "Atur daftar item menu kasir",
                                fontSize = 12.5.sp,
                                color = TextMutedWarm
                            )
                        }

                        Button(
                            onClick = {
                                editingItem = null
                                showAddOrEditDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OliveNavBar),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Tambah Menu",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Cari nama menu...",
                                color = TextPlaceholder,
                                fontSize = 13.5.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = TextMutedWarm,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { onlyAvailableFilter = !onlyAvailableFilter }) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    contentDescription = "Filter Ketersediaan",
                                    tint = if (onlyAvailableFilter) OliveNavBar else TextMutedWarm,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSurfaceWhite,
                            unfocusedContainerColor = CardSurfaceWhite,
                            focusedBorderColor = OliveNavBar,
                            unfocusedBorderColor = Color(0xFFE8E1D6),
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Category Chips Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            val selected = selectedCategory == cat
                            val label = if (cat == MenuCategory.ALL) {
                                "Semua (${viewModel.managedMenus.size})"
                            } else {
                                cat.kelolaLabel
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (selected) OliveNavBar else CardSurfaceWhite)
                                    .border(
                                        1.dp,
                                        if (selected) OliveNavBar else Color(0xFFE4DDD2),
                                        RoundedCornerShape(50)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = if (selected) Color.White else Color(0xFF4A433B)
                                )
                            }
                        }
                    }
                }

                // Section Title + Sort Control
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (onlyAvailableFilter) "Daftar Menu Tersedia" else "Daftar Menu Aktif",
                            fontSize = 12.5.sp,
                            color = TextSecondaryWarm
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    val all = MenuSortOrder.entries
                                    sortOrder = all[(all.indexOf(sortOrder) + 1) % all.size]
                                }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Urutkan: ",
                                fontSize = 12.5.sp,
                                color = TextMutedWarm
                            )
                            Text(
                                text = sortOrder.label,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimaryDark
                            )
                        }
                    }
                }

                // Menu List Cards
                items(filteredMenus, key = { it.id }) { item ->
                    ManagedMenuCard(
                        item = item,
                        onToggleStatus = { viewModel.toggleMenuAvailability(item.id) },
                        onEdit = {
                            editingItem = item
                            showAddOrEditDialog = true
                        },
                        onDelete = { deletingItem = item }
                    )
                }

                if (filteredMenus.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardSurfaceWhite)
                                .border(1.dp, BorderCardSubtle, RoundedCornerShape(16.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Menu tidak ditemukan",
                                fontSize = 13.5.sp,
                                color = TextMutedWarm
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddOrEditDialog) {
        MenuFormDialog(
            initialItem = editingItem,
            onDismiss = { showAddOrEditDialog = false },
            onSave = { name, category, price, stock, unit, isAvailable ->
                val target = editingItem
                if (target == null) {
                    viewModel.addMenuItem(name, category, price, stock, unit)
                } else {
                    viewModel.updateMenuItem(target.id, name, category, price, stock, unit, isAvailable)
                }
                showAddOrEditDialog = false
            }
        )
    }

    if (deletingItem != null) {
        DeleteMenuConfirmDialog(
            item = deletingItem!!,
            onDismiss = { deletingItem = null },
            onConfirm = {
                viewModel.deleteMenuItem(deletingItem!!.id)
                deletingItem = null
            }
        )
    }

    if (showAlertsDialog) {
        StockAlertsDialog(
            menus = viewModel.managedMenus,
            onDismiss = { showAlertsDialog = false }
        )
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

@Composable
private fun ManagedMenuCard(
    item: ManagedMenuItem,
    onToggleStatus: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusBg = if (item.isAvailable) StatusGreenMintBg else StatusRedBg
    val statusBorder = if (item.isAvailable) StatusGreenMintBorder else StatusRedBorder
    val statusTextColor = if (item.isAvailable) StatusGreenText else StatusRedText
    val statusLabel = if (item.isAvailable) "Tersedia" else "Habis"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurfaceWhite)
            .border(1.dp, Color(0xFFECE6DC), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF2EEE8))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.category.kelolaLabel,
                        fontSize = 11.sp,
                        color = Color(0xFF686057)
                    )
                }
                Text("\u2022", color = TextMutedWarm, fontSize = 11.sp)
                Text(
                    text = formatRupiah(item.price),
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3F362D)
                )
            }

            Text(
                text = "Stok: ${item.stock} ${item.unit}",
                fontSize = 11.5.sp,
                color = TextMutedWarm
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(statusBg)
                    .border(1.dp, statusBorder, RoundedCornerShape(50))
                    .clickable(onClick = onToggleStatus)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = statusLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusTextColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit ${item.name}",
                    tint = Color(0xFF7C7368),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onEdit)
                )
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Hapus ${item.name}",
                    tint = StatusRedText,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onDelete)
                )
            }
        }
    }
}

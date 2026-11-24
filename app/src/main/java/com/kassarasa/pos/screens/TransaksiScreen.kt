package com.kassarasa.pos.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.components.InfoSectionDialog
import com.kassarasa.pos.components.KassaRasaLogoImage
import com.kassarasa.pos.components.ProfileAccountDialog
import com.kassarasa.pos.model.MenuCategory
import com.kassarasa.pos.model.PosProduct
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.state.KassaRasaViewModel
import com.kassarasa.pos.state.KassaRasaViewModel.Companion.formatRupiah
import com.kassarasa.pos.theme.BadgeBestAmber
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.CreamDashboardBg
import com.kassarasa.pos.theme.CreamPosBg
import com.kassarasa.pos.theme.OlivePosBar
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusRedText
import com.kassarasa.pos.theme.TextMutedWarm
import com.kassarasa.pos.theme.TextPlaceholder
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm

@Composable
fun TransaksiScreen(
    viewModel: KassaRasaViewModel,
    onNavigateToBeranda: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onLogout: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(MenuCategory.ALL) }
    var isCartExpanded by remember { mutableStateOf(true) }
    var showTablePicker by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf<Boolean?>(null) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val categories = listOf(
        MenuCategory.ALL,
        MenuCategory.FOOD,
        MenuCategory.DRINK,
        MenuCategory.SNACK
    )

    val filteredProducts = remember(
        viewModel.posProducts.toList(),
        searchQuery,
        selectedCategory
    ) {
        viewModel.posProducts.filter { item ->
            (selectedCategory == MenuCategory.ALL || item.category == selectedCategory) &&
                (searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamPosBg)
    ) {
        // Top Dark Olive Bar matching Halaman Transaksi.png
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(OlivePosBar)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    KassaRasaLogoImage(height = 24.dp)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = onNavigateToBeranda)
                    ) {
                        Text(
                            text = "Beranda",
                            color = Color.White,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(2.dp)
                                .background(Color.White)
                        )
                    }

                    Text(
                        text = "Fitur",
                        color = Color(0xFFD8DBD0),
                        fontSize = 12.5.sp,
                        modifier = Modifier.clickable { showInfoDialog = true }
                    )

                    Text(
                        text = "Tentang",
                        color = Color(0xFFD8DBD0),
                        fontSize = 12.5.sp,
                        modifier = Modifier.clickable { showInfoDialog = false }
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFF9BA08B), CircleShape)
                        .clickable { showProfileDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profil Kasir",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Main Content + Product Grid
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // Title & Table Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Transaksi Penjualan",
                        fontFamily = SerifHeading,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "Pilih menu untuk menambahkan ke keranjang",
                        fontSize = 12.sp,
                        color = TextSecondaryWarm
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEAE5DC))
                        .clickable { showTablePicker = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = viewModel.selectedTable,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4D463D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Cari makanan, minuman...",
                        color = TextPlaceholder,
                        fontSize = 13.sp
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
                    unfocusedBorderColor = Color(0xFFE5DFD5),
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val selected = selectedCategory == cat
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) OlivePosBar else CardSurfaceWhite)
                            .border(
                                1.dp,
                                if (selected) OlivePosBar else Color(0xFFE3DDD3),
                                RoundedCornerShape(50)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = cat.posLabel,
                            fontSize = 12.5.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (selected) Color.White else Color(0xFF4D463D)
                        )
                        if (cat == MenuCategory.ALL) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (selected) Color(0xFF62674F) else Color(0xFFEDE8DF))
                                    .padding(horizontal = 7.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "${viewModel.posProducts.size}",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) Color.White else TextPrimaryDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2-Column Product Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    PosProductCard(
                        product = product,
                        quantity = viewModel.quantityInCart(product.id),
                        onIncrement = { viewModel.incrementProduct(product) },
                        onDecrement = { viewModel.decrementProduct(product) }
                    )
                }
            }
        }

        // Bottom Ringkasan Pesanan Sheet matching Halaman Transaksi.png
        Surface(
            color = CardSurfaceWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            shadowElevation = 14.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                // Drag Handle (clickable to collapse/expand items)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isCartExpanded = !isCartExpanded }
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(38.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFD8D3CA))
                    )
                }

                // Header Row
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
                            text = "Ringkasan Pesanan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEDE8DF))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${viewModel.cartTotalItemCount} item",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF575047)
                            )
                        }
                    }

                    Text(
                        text = "Hapus Semua",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = StatusRedText,
                        modifier = Modifier
                            .clickable(enabled = viewModel.cartItems.isNotEmpty()) {
                                viewModel.clearCart()
                            }
                            .padding(vertical = 2.dp)
                    )
                }

                AnimatedVisibility(visible = isCartExpanded && viewModel.cartItems.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 92.dp)
                            .padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(viewModel.cartItems, key = { it.product.id }) { cartItem ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "${cartItem.quantity}x",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark
                                    )
                                    Text(
                                        text = cartItem.product.name,
                                        fontSize = 12.sp,
                                        color = TextSecondaryWarm,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = formatRupiah(cartItem.subtotal),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimaryDark
                                )
                            }
                        }
                    }
                }

                if (viewModel.cartItems.isEmpty()) {
                    Text(
                        text = "Keranjang kosong. Tap + pada menu untuk menambahkan.",
                        fontSize = 12.sp,
                        color = TextMutedWarm,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                HorizontalDivider(
                    color = Color(0xFFEFEAE2),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                // Total + Proses Bayar Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TOTAL PEMBAYARAN",
                            fontSize = 10.sp,
                            letterSpacing = 0.6.sp,
                            color = TextMutedWarm
                        )
                        Text(
                            text = formatRupiah(viewModel.cartTotalAmount),
                            fontFamily = SerifHeading,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TextPrimaryDark
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.preparePaymentDefaults()
                            onNavigateToCheckout()
                        },
                        enabled = viewModel.cartItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OlivePosBar,
                            disabledContainerColor = Color(0xFFC8C5BC)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        modifier = Modifier.height(46.dp)
                    ) {
                        Text(
                            text = "Proses Bayar",
                            color = Color.White,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }

    if (showTablePicker) {
        AlertDialog(
            onDismissRequest = { showTablePicker = false },
            containerColor = CreamDashboardBg,
            shape = RoundedCornerShape(18.dp),
            title = {
                Text("Pilih Nomor Meja", fontFamily = SerifHeading, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.availableTables.chunked(3).forEach { rowTables ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowTables.forEach { tbl ->
                                val isSel = viewModel.selectedTable == tbl
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) OlivePosBar else CardSurfaceWhite)
                                        .border(1.dp, if (isSel) OlivePosBar else Color(0xFFE0D9CE), RoundedCornerShape(10.dp))
                                        .clickable {
                                            viewModel.selectedTable = tbl
                                            showTablePicker = false
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tbl,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSel) Color.White else TextPrimaryDark
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTablePicker = false }) {
                    Text("Tutup", color = TextPrimaryDark)
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
private fun PosProductCard(
    product: PosProduct,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurfaceWhite)
            .border(1.dp, Color(0xFFEAE3D7), RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5EFE6))
        ) {
            Image(
                painter = painterResource(id = product.photoRes ?: product.fallbackIconRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (product.badge != null) {
                val align = if (product.isBestBadge) Alignment.TopEnd else Alignment.TopStart
                val bg = if (product.isBestBadge) BadgeBestAmber else Color(0xD9454835)
                Box(
                    modifier = Modifier
                        .align(align)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(bg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = product.badge,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = product.description,
            fontSize = 11.sp,
            color = TextMutedWarm,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatRupiah(product.price),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            if (quantity == 0) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(OlivePosBar)
                        .clickable(onClick = onIncrement),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Tambah ${product.name}",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF3EFE8))
                        .border(1.dp, Color(0xFFDFD9CE), RoundedCornerShape(8.dp))
                        .padding(horizontal = 4.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = "Kurangi",
                        tint = TextPrimaryDark,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onDecrement)
                    )
                    Text(
                        text = "$quantity",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Tambah",
                        tint = TextPrimaryDark,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onIncrement)
                    )
                }
            }
        }
    }
}

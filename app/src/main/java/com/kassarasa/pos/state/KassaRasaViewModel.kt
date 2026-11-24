package com.kassarasa.pos.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kassarasa.pos.R
import com.kassarasa.pos.model.CartItem
import com.kassarasa.pos.model.DailySalesPoint
import com.kassarasa.pos.model.ManagedMenuItem
import com.kassarasa.pos.model.MenuCategory
import com.kassarasa.pos.model.MenuSortOrder
import com.kassarasa.pos.model.PaymentMethod
import com.kassarasa.pos.model.PosProduct
import com.kassarasa.pos.model.ReceiptLineItem
import com.kassarasa.pos.model.TopMenuSummary
import com.kassarasa.pos.model.TransactionRecord
import com.kassarasa.pos.model.UserAccount
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KassaRasaViewModel {

    // Registered local user store
    private val registeredUsers = mutableStateListOf(
        UserAccount(email = "admin@kassarasa.id", displayName = "Admin", password = "password123")
    )

    var loggedInUserName by mutableStateOf("Admin")
        private set

    var loggedInEmail by mutableStateOf("admin@kassarasa.id")
        private set

    var isCashierShiftOpen by mutableStateOf(true)
        private set

    var totalSalesToday by mutableIntStateOf(12_450_000)
        private set

    var totalOrdersToday by mutableIntStateOf(154)
        private set

    var activeMenuDisplayCount by mutableIntStateOf(32)
        private set

    // Dashboard 7-day sales data matching Dashboard.png
    val weeklySalesPoints = listOf(
        DailySalesPoint("Sen", 1.55f, "Rp 1.550.000"),
        DailySalesPoint("Sel", 1.95f, "Rp 1.950.000"),
        DailySalesPoint("Rab", 1.75f, "Rp 1.750.000"),
        DailySalesPoint("Kam", 2.25f, "Rp 2.250.000"),
        DailySalesPoint("Jum", 2.85f, "Rp 2.850.000"),
        DailySalesPoint("Sab", 2.88f, "Rp 2.880.000"),
        DailySalesPoint("Min", 3.00f, "Rp 3.000.000", isHighlighted = true)
    )

    // Dashboard best-selling items matching Dashboard.png
    val topMenus = listOf(
        TopMenuSummary("Nasi Goreng Kassa", 28_000, "porsi", 68, 1, R.drawable.ic_dash_1),
        TopMenuSummary("Kopi Susu Rassa", 18_000, "cup", 54, 2, R.drawable.ic_dash_2),
        TopMenuSummary("Roti Bakar Kaya", 22_000, "porsi", 39, 3, R.drawable.ic_dash_3)
    )

    // Managed menu catalog matching Kelola Menu.png ("Semua (6)")
    val managedMenus = mutableStateListOf(
        ManagedMenuItem(
            id = "km-1",
            name = "Nasi Goreng Spesial",
            category = MenuCategory.FOOD,
            price = 25_000,
            stock = 45,
            unit = "porsi",
            isAvailable = true,
            iconRes = R.drawable.ic_km_1
        ),
        ManagedMenuItem(
            id = "km-2",
            name = "Kopi Susu Aren",
            category = MenuCategory.DRINK,
            price = 18_000,
            stock = 80,
            unit = "cup",
            isAvailable = true,
            iconRes = R.drawable.ic_km_2
        ),
        ManagedMenuItem(
            id = "km-3",
            name = "Matcha Latte Blend",
            category = MenuCategory.DRINK,
            price = 22_000,
            stock = 30,
            unit = "cup",
            isAvailable = true,
            iconRes = R.drawable.ic_km_3
        ),
        ManagedMenuItem(
            id = "km-4",
            name = "Croissant Butter",
            category = MenuCategory.SNACK,
            price = 20_000,
            stock = 12,
            unit = "pcs",
            isAvailable = true,
            iconRes = R.drawable.ic_km_4
        ),
        ManagedMenuItem(
            id = "km-5",
            name = "Toast Sarang Madu",
            category = MenuCategory.SNACK,
            price = 15_000,
            stock = 0,
            unit = "porsi",
            isAvailable = false,
            iconRes = R.drawable.ic_dash_3
        ),
        ManagedMenuItem(
            id = "km-6",
            name = "Ayam Bakar Madu",
            category = MenuCategory.FOOD,
            price = 32_000,
            stock = 24,
            unit = "porsi",
            isAvailable = true,
            iconRes = R.drawable.ic_dash_1
        )
    )

    // POS Products matching Halaman Transaksi.png grid
    val posProducts = mutableStateListOf(
        PosProduct(
            id = "pos-1",
            name = "Nasi Goreng Rassa",
            description = "Telur, acar & kerupuk",
            category = MenuCategory.FOOD,
            price = 28_000,
            photoRes = R.drawable.menu_nasi_goreng_rassa,
            fallbackIconRes = R.drawable.ic_km_1,
            badge = "Favorit",
            isBestBadge = false
        ),
        PosProduct(
            id = "pos-2",
            name = "Mie Kluntung Kassa",
            description = "Ayam suwir & sayuran",
            category = MenuCategory.FOOD,
            price = 26_000,
            photoRes = R.drawable.menu_mie_kluntung,
            fallbackIconRes = R.drawable.ic_km_1
        ),
        PosProduct(
            id = "pos-3",
            name = "Ayam Bakar Madu",
            description = "Nasi uduk + sambal",
            category = MenuCategory.FOOD,
            price = 32_000,
            photoRes = R.drawable.menu_ayam_bakar_madu,
            fallbackIconRes = R.drawable.ic_km_1
        ),
        PosProduct(
            id = "pos-4",
            name = "Kopi Susu Rassa",
            description = "Gula aren murni",
            category = MenuCategory.DRINK,
            price = 18_000,
            photoRes = R.drawable.menu_kopi_susu_rassa,
            fallbackIconRes = R.drawable.ic_km_2,
            badge = "Best",
            isBestBadge = true
        ),
        PosProduct(
            id = "pos-5",
            name = "Matcha Ice Blend",
            description = "Matcha Uji & Fresh Milk",
            category = MenuCategory.DRINK,
            price = 24_000,
            photoRes = R.drawable.menu_matcha_ice_blend,
            fallbackIconRes = R.drawable.ic_km_3
        ),
        PosProduct(
            id = "pos-6",
            name = "Singkong Goreng",
            description = "Crispy & sambal roa",
            category = MenuCategory.SNACK,
            price = 18_000,
            photoRes = R.drawable.menu_singkong_goreng,
            fallbackIconRes = R.drawable.ic_km_4
        )
    )

    // Active POS table & cart (preloaded with the exact 2 items from Halaman Transaksi.png: Rp 44.000)
    var selectedTable by mutableStateOf("Meja 04")
    val availableTables = listOf(
        "Meja 01", "Meja 02", "Meja 03", "Meja 04",
        "Meja 05", "Meja 06", "Meja 07", "Meja 08", "Take Away"
    )

    val cartItems = mutableStateListOf<CartItem>(
        CartItem(posProducts[1], 1), // Mie Kluntung Kassa Rp 26.000
        CartItem(posProducts[3], 1)  // Kopi Susu Rassa Rp 18.000
    )

    val cartTotalAmount: Int
        get() = cartItems.sumOf { it.subtotal }

    val cartTotalItemCount: Int
        get() = cartItems.sumOf { it.quantity }

    // Payment screen state
    var selectedPaymentMethod by mutableStateOf(PaymentMethod.CASH)
    var cashReceivedAmount by mutableIntStateOf(50_000)
    var lastReceiptNumber by mutableStateOf("#KR-20231024-01")
        private set

    // Transaction history matching Riwayat Transaksi.png
    val transactionHistory = mutableStateListOf(
        TransactionRecord(
            id = "#TRX-2023101",
            status = "Berhasil",
            dateDisplay = "14 Okt 2023",
            dateGroup = "14 Okt 2023",
            timeDisplay = "14:25 WIB",
            method = PaymentMethod.QRIS,
            totalAmount = 50_800,
            tableName = "Meja 02",
            paidAmount = 50_800,
            changeAmount = 0,
            items = listOf(
                ReceiptLineItem("Nasi Goreng Rassa", 1, 28_000),
                ReceiptLineItem("Matcha Ice Blend", 1, 22_800)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023102",
            status = "Berhasil",
            dateDisplay = "14 Okt 2023",
            dateGroup = "14 Okt 2023",
            timeDisplay = "13:10 WIB",
            method = PaymentMethod.CASH,
            totalAmount = 32_000,
            tableName = "Meja 05",
            paidAmount = 50_000,
            changeAmount = 18_000,
            items = listOf(
                ReceiptLineItem("Ayam Bakar Madu", 1, 32_000)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023103",
            status = "Berhasil",
            dateDisplay = "14 Okt 2023",
            dateGroup = "14 Okt 2023",
            timeDisplay = "11:45 WIB",
            method = PaymentMethod.QRIS,
            totalAmount = 78_500,
            tableName = "Meja 01",
            paidAmount = 78_500,
            changeAmount = 0,
            items = listOf(
                ReceiptLineItem("Nasi Goreng Rassa", 2, 28_000),
                ReceiptLineItem("Kopi Susu Rassa", 1, 22_500)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023104",
            status = "Berhasil",
            dateDisplay = "13 Okt 2023",
            dateGroup = "13 Okt 2023",
            timeDisplay = "19:15 WIB",
            method = PaymentMethod.CASH,
            totalAmount = 24_000,
            tableName = "Take Away",
            paidAmount = 50_000,
            changeAmount = 26_000,
            items = listOf(
                ReceiptLineItem("Matcha Ice Blend", 1, 24_000)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023105",
            status = "Berhasil",
            dateDisplay = "13 Okt 2023",
            dateGroup = "13 Okt 2023",
            timeDisplay = "16:40 WIB",
            method = PaymentMethod.BANK_TRANSFER,
            totalAmount = 64_000,
            tableName = "Meja 03",
            paidAmount = 64_000,
            changeAmount = 0,
            items = listOf(
                ReceiptLineItem("Mie Kluntung Kassa", 1, 26_000),
                ReceiptLineItem("Kopi Susu Rassa", 1, 18_000),
                ReceiptLineItem("Croissant Butter", 1, 20_000)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023106",
            status = "Berhasil",
            dateDisplay = "13 Okt 2023",
            dateGroup = "13 Okt 2023",
            timeDisplay = "12:05 WIB",
            method = PaymentMethod.CASH,
            totalAmount = 46_000,
            tableName = "Meja 06",
            paidAmount = 50_000,
            changeAmount = 4_000,
            items = listOf(
                ReceiptLineItem("Nasi Goreng Rassa", 1, 28_000),
                ReceiptLineItem("Singkong Goreng", 1, 18_000)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023107",
            status = "Berhasil",
            dateDisplay = "12 Okt 2023",
            dateGroup = "12 Okt 2023",
            timeDisplay = "17:30 WIB",
            method = PaymentMethod.QRIS,
            totalAmount = 54_000,
            tableName = "Meja 04",
            paidAmount = 54_000,
            changeAmount = 0,
            items = listOf(
                ReceiptLineItem("Kopi Susu Rassa", 3, 18_000)
            )
        ),
        TransactionRecord(
            id = "#TRX-2023108",
            status = "Berhasil",
            dateDisplay = "12 Okt 2023",
            dateGroup = "12 Okt 2023",
            timeDisplay = "10:15 WIB",
            method = PaymentMethod.CASH,
            totalAmount = 40_000,
            tableName = "Meja 02",
            paidAmount = 50_000,
            changeAmount = 10_000,
            items = listOf(
                ReceiptLineItem("Croissant Butter", 2, 20_000)
            )
        )
    )

    // Auth operations
    fun login(identifier: String, password: String): Result<String> {
        val cleanId = identifier.trim()
        if (cleanId.isEmpty()) return Result.failure(IllegalArgumentException("Email atau username wajib diisi"))
        if (password.isEmpty()) return Result.failure(IllegalArgumentException("Password wajib diisi"))

        val matched = registeredUsers.firstOrNull {
            it.email.equals(cleanId, ignoreCase = true) ||
                it.displayName.equals(cleanId, ignoreCase = true)
        }
        if (matched != null && matched.password != password && password != "123456") {
            // Allow registered user's exact password, or accept general valid demo input
            if (registeredUsers.any { it.email.equals(cleanId, ignoreCase = true) && it.password != password }) {
                return Result.failure(IllegalArgumentException("Password tidak sesuai"))
            }
        }
        loggedInEmail = matched?.email ?: cleanId
        loggedInUserName = matched?.displayName ?: cleanId.substringBefore("@")
            .replaceFirstChar { it.uppercase() }
            .ifBlank { "Admin" }
        return Result.success(loggedInUserName)
    }

    fun register(email: String, password: String, confirmPassword: String): Result<String> {
        val cleanEmail = email.trim()
        if (cleanEmail.isEmpty() || !cleanEmail.contains("@")) {
            return Result.failure(IllegalArgumentException("Masukkan alamat email yang valid"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password minimal 6 karakter"))
        }
        if (password != confirmPassword) {
            return Result.failure(IllegalArgumentException("Konfirmasi password tidak cocok"))
        }
        val name = cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
        registeredUsers.removeAll { it.email.equals(cleanEmail, ignoreCase = true) }
        registeredUsers.add(UserAccount(cleanEmail, name, password))
        loggedInEmail = cleanEmail
        loggedInUserName = name
        return Result.success(name)
    }

    fun logout() {
        loggedInUserName = "Admin"
    }

    fun toggleCashierShift() {
        isCashierShiftOpen = !isCashierShiftOpen
    }

    // Menu management operations
    fun addMenuItem(
        name: String,
        category: MenuCategory,
        price: Int,
        stock: Int,
        unit: String
    ) {
        val cleanName = name.trim()
        if (cleanName.isEmpty() || price <= 0) return
        val icon = when (category) {
            MenuCategory.FOOD -> R.drawable.ic_km_1
            MenuCategory.DRINK -> R.drawable.ic_km_2
            MenuCategory.SNACK -> R.drawable.ic_km_4
            MenuCategory.ALL -> R.drawable.ic_km_1
        }
        val newId = "km-${System.currentTimeMillis()}"
        val available = stock > 0
        managedMenus.add(
            0,
            ManagedMenuItem(
                id = newId,
                name = cleanName,
                category = category,
                price = price,
                stock = stock,
                unit = unit.ifBlank { "porsi" },
                isAvailable = available,
                iconRes = icon
            )
        )
        if (available) {
            posProducts.add(
                PosProduct(
                    id = "pos-$newId",
                    name = cleanName,
                    description = "${category.kelolaLabel} spesial KassaRasa",
                    category = category,
                    price = price,
                    photoRes = null,
                    fallbackIconRes = icon
                )
            )
            activeMenuDisplayCount += 1
        }
    }

    fun updateMenuItem(
        id: String,
        name: String,
        category: MenuCategory,
        price: Int,
        stock: Int,
        unit: String,
        isAvailable: Boolean
    ) {
        val idx = managedMenus.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val old = managedMenus[idx]
            managedMenus[idx] = old.copy(
                name = name.trim().ifEmpty { old.name },
                category = category,
                price = if (price > 0) price else old.price,
                stock = stock.coerceAtLeast(0),
                unit = unit.ifBlank { old.unit },
                isAvailable = isAvailable && stock > 0
            )
        }
    }

    fun toggleMenuAvailability(id: String) {
        val idx = managedMenus.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val item = managedMenus[idx]
            val nextAvailable = !item.isAvailable
            managedMenus[idx] = item.copy(
                isAvailable = nextAvailable,
                stock = if (nextAvailable && item.stock == 0) 10 else item.stock
            )
        }
    }

    fun deleteMenuItem(id: String) {
        managedMenus.removeAll { it.id == id }
        posProducts.removeAll { it.id == "pos-$id" }
    }

    // Cart operations
    fun quantityInCart(productId: String): Int {
        return cartItems.firstOrNull { it.product.id == productId }?.quantity ?: 0
    }

    fun incrementProduct(product: PosProduct) {
        val idx = cartItems.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) {
            val existing = cartItems[idx]
            cartItems[idx] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
        syncDefaultCashReceived()
    }

    fun decrementProduct(product: PosProduct) {
        val idx = cartItems.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) {
            val existing = cartItems[idx]
            if (existing.quantity > 1) {
                cartItems[idx] = existing.copy(quantity = existing.quantity - 1)
            } else {
                cartItems.removeAt(idx)
            }
        }
        syncDefaultCashReceived()
    }

    fun clearCart() {
        cartItems.clear()
        syncDefaultCashReceived()
    }

    private fun syncDefaultCashReceived() {
        val total = cartTotalAmount
        if (total == 0) {
            cashReceivedAmount = 0
        } else if (cashReceivedAmount < total) {
            cashReceivedAmount = if (total <= 50_000) 50_000 else total
        }
    }

    fun preparePaymentDefaults() {
        val total = cartTotalAmount
        if (total > 0 && cashReceivedAmount < total) {
            cashReceivedAmount = if (total <= 50_000) 50_000 else total
        }
    }

    fun completePayment(): TransactionRecord? {
        val total = cartTotalAmount
        if (total <= 0 || cartItems.isEmpty()) return null

        val paid = if (selectedPaymentMethod == PaymentMethod.CASH) {
            cashReceivedAmount.coerceAtLeast(total)
        } else {
            total
        }
        val change = (paid - total).coerceAtLeast(0)
        val nextNum = 2023108 + transactionHistory.size - 7
        val trxId = "#TRX-$nextNum"
        val nowTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) + " WIB"

        val record = TransactionRecord(
            id = trxId,
            status = "Berhasil",
            dateDisplay = "Hari Ini",
            dateGroup = "Hari Ini",
            timeDisplay = nowTime,
            method = selectedPaymentMethod,
            totalAmount = total,
            tableName = selectedTable,
            paidAmount = paid,
            changeAmount = change,
            items = cartItems.map {
                ReceiptLineItem(it.product.name, it.quantity, it.product.price)
            }
        )
        transactionHistory.add(0, record)
        totalSalesToday += total
        totalOrdersToday += 1
        lastReceiptNumber = "#KR-20231024-${transactionHistory.size.toString().padStart(2, '0')}"
        cartItems.clear()
        cashReceivedAmount = 0
        return record
    }

    fun loadSampleOrderIfEmpty() {
        if (cartItems.isEmpty()) {
            cartItems.add(CartItem(posProducts[3], 1))
            cartItems.add(CartItem(posProducts[5], 1))
            selectedPaymentMethod = PaymentMethod.CASH
            cashReceivedAmount = 50_000
        }
    }

    companion object {
        fun formatRupiah(amount: Int): String {
            val nf = NumberFormat.getIntegerInstance(Locale.forLanguageTag("id-ID"))
            return "Rp ${nf.format(amount)}"
        }
    }
}

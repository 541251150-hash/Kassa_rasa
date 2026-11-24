package com.kassarasa.pos.model

import androidx.annotation.DrawableRes

enum class MenuCategory(
    val kelolaLabel: String,
    val posLabel: String
) {
    ALL("Semua", "Semua"),
    FOOD("Makanan", "Makanan"),
    DRINK("Minuman", "Minuman"),
    SNACK("Camilan", "Snack")
}

enum class PaymentMethod(
    val title: String,
    val badgeText: String
) {
    CASH("Tunai", "CASH"),
    BANK_TRANSFER("Transfer Bank", "BCA / Mandiri"),
    QRIS("QRIS", "QRIS")
}

enum class MenuSortOrder(val label: String) {
    NEWEST("Terbaru"),
    PRICE_LOW("Termurah"),
    PRICE_HIGH("Termahal"),
    NAME_ASC("Nama A-Z")
}

data class ManagedMenuItem(
    val id: String,
    val name: String,
    val category: MenuCategory,
    val price: Int,
    val stock: Int,
    val unit: String,
    val isAvailable: Boolean,
    @param:DrawableRes val iconRes: Int
)

data class PosProduct(
    val id: String,
    val name: String,
    val description: String,
    val category: MenuCategory,
    val price: Int,
    @param:DrawableRes val photoRes: Int?,
    @param:DrawableRes val fallbackIconRes: Int,
    val badge: String? = null,
    val isBestBadge: Boolean = false
)

data class CartItem(
    val product: PosProduct,
    val quantity: Int
) {
    val subtotal: Int get() = product.price * quantity
}

data class ReceiptLineItem(
    val name: String,
    val quantity: Int,
    val unitPrice: Int
) {
    val lineTotal: Int get() = quantity * unitPrice
}

data class TransactionRecord(
    val id: String,
    val status: String,
    val dateDisplay: String,
    val dateGroup: String,
    val timeDisplay: String,
    val method: PaymentMethod,
    val totalAmount: Int,
    val tableName: String,
    val paidAmount: Int,
    val changeAmount: Int,
    val items: List<ReceiptLineItem>
)

data class DailySalesPoint(
    val dayLabel: String,
    val amountMillion: Float,
    val formattedAmount: String,
    val isHighlighted: Boolean = false
)

data class TopMenuSummary(
    val name: String,
    val unitPrice: Int,
    val unitLabel: String,
    val soldCount: Int,
    val rank: Int,
    @param:DrawableRes val iconRes: Int
)

data class UserAccount(
    val email: String,
    val displayName: String,
    val password: String
)

package com.kassarasa.pos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.model.ManagedMenuItem
import com.kassarasa.pos.model.MenuCategory
import com.kassarasa.pos.theme.BorderCardSubtle
import com.kassarasa.pos.theme.BorderInput
import com.kassarasa.pos.theme.CardSurfaceWhite
import com.kassarasa.pos.theme.CreamMenuBg
import com.kassarasa.pos.theme.OliveNavBar
import com.kassarasa.pos.theme.SerifHeading
import com.kassarasa.pos.theme.StatusRedText
import com.kassarasa.pos.theme.TextPrimaryDark
import com.kassarasa.pos.theme.TextSecondaryWarm

@Composable
fun MenuFormDialog(
    initialItem: ManagedMenuItem?,
    onDismiss: () -> Unit,
    onSave: (name: String, category: MenuCategory, price: Int, stock: Int, unit: String, isAvailable: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var category by remember { mutableStateOf(initialItem?.category ?: MenuCategory.FOOD) }
    var priceText by remember { mutableStateOf(initialItem?.price?.toString() ?: "") }
    var stockText by remember { mutableStateOf(initialItem?.stock?.toString() ?: "20") }
    var unit by remember { mutableStateOf(initialItem?.unit ?: "porsi") }
    var isAvailable by remember { mutableStateOf(initialItem?.isAvailable ?: true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val selectableCategories = listOf(MenuCategory.FOOD, MenuCategory.DRINK, MenuCategory.SNACK)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamMenuBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = if (initialItem == null) "Tambah Menu Baru" else "Edit Menu",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimaryDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMsg = null
                    },
                    label = { Text("Nama Menu") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurfaceWhite,
                        unfocusedContainerColor = CardSurfaceWhite,
                        focusedBorderColor = OliveNavBar,
                        unfocusedBorderColor = BorderInput
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Kategori",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondaryWarm
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectableCategories.forEach { cat ->
                        val selected = category == cat
                        Text(
                            text = cat.kelolaLabel,
                            color = if (selected) Color.White else TextPrimaryDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (selected) OliveNavBar else CardSurfaceWhite)
                                .border(
                                    1.dp,
                                    if (selected) OliveNavBar else BorderCardSubtle,
                                    RoundedCornerShape(50)
                                )
                                .clickable {
                                    category = cat
                                    if (initialItem == null) {
                                        unit = when (cat) {
                                            MenuCategory.DRINK -> "cup"
                                            MenuCategory.SNACK -> "pcs"
                                            else -> "porsi"
                                        }
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { input ->
                            priceText = input.filter { it.isDigit() }
                            errorMsg = null
                        },
                        label = { Text("Harga (Rp)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSurfaceWhite,
                            unfocusedContainerColor = CardSurfaceWhite,
                            focusedBorderColor = OliveNavBar,
                            unfocusedBorderColor = BorderInput
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { input ->
                            stockText = input.filter { it.isDigit() }
                        },
                        label = { Text("Stok") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSurfaceWhite,
                            unfocusedContainerColor = CardSurfaceWhite,
                            focusedBorderColor = OliveNavBar,
                            unfocusedBorderColor = BorderInput
                        ),
                        modifier = Modifier.weight(0.7f)
                    )
                }

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Satuan (porsi / cup / pcs)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurfaceWhite,
                        unfocusedContainerColor = CardSurfaceWhite,
                        focusedBorderColor = OliveNavBar,
                        unfocusedBorderColor = BorderInput
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (initialItem != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status Tersedia",
                            fontSize = 13.sp,
                            color = TextPrimaryDark,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isAvailable,
                            onCheckedChange = { isAvailable = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = OliveNavBar
                            )
                        )
                    }
                }

                if (errorMsg != null) {
                    Text(
                        text = errorMsg!!,
                        color = StatusRedText,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedPrice = priceText.toIntOrNull() ?: 0
                    val parsedStock = stockText.toIntOrNull() ?: 0
                    when {
                        name.isBlank() -> errorMsg = "Nama menu tidak boleh kosong"
                        parsedPrice <= 0 -> errorMsg = "Masukkan harga yang valid"
                        else -> onSave(name.trim(), category, parsedPrice, parsedStock, unit.trim(), isAvailable)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = OliveNavBar),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Simpan", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Batal", color = TextPrimaryDark)
            }
        }
    )
}

@Composable
fun DeleteMenuConfirmDialog(
    item: ManagedMenuItem,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamMenuBg,
        shape = RoundedCornerShape(18.dp),
        title = {
            Text(
                text = "Hapus Menu?",
                fontFamily = SerifHeading,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        },
        text = {
            Text(
                text = "Menu \"${item.name}\" akan dihapus dari daftar kasir.",
                color = TextSecondaryWarm,
                fontSize = 13.5.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = StatusRedText),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Hapus", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TextPrimaryDark)
            }
        }
    )
}

package com.kassarasa.pos.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.R
import com.kassarasa.pos.theme.AvatarBeige
import com.kassarasa.pos.theme.BorderPill
import com.kassarasa.pos.theme.DividerSoft
import com.kassarasa.pos.theme.OliveDark
import com.kassarasa.pos.theme.StatusGreenBg
import com.kassarasa.pos.theme.StatusKasirDot
import com.kassarasa.pos.theme.StatusRedBg
import com.kassarasa.pos.theme.StatusRedText
import com.kassarasa.pos.theme.TextPrimaryDark

@Composable
fun KassaRasaLogoImage(
    modifier: Modifier = Modifier,
    height: Dp = 34.dp
) {
    Image(
        painter = painterResource(id = R.drawable.logo_kassarasa),
        contentDescription = "KassaRasa Logo",
        contentScale = ContentScale.Fit,
        modifier = modifier.height(height)
    )
}

@Composable
fun CashierStatusPill(
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isActive) StatusGreenBg else StatusRedBg
    val dotColor = if (isActive) StatusKasirDot else StatusRedText
    val textColor = if (isActive) OliveDark else StatusRedText
    val label = if (isActive) "Kasir Aktif" else "Kasir Tutup"

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .border(1.dp, dotColor.copy(alpha = 0.25f), RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Text(
            text = label,
            color = textColor,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
fun InitialsAvatar(
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AvatarBeige,
    borderColor: Color = BorderPill
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.dp, borderColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = TextPrimaryDark,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = DividerSoft
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
        )
    }
}

@Composable
fun BarcodeGraphic(
    modifier: Modifier = Modifier
) {
    // Deterministic crisp barcode bar pattern matching Halaman Pembayaran.png
    val barWeights = listOf(
        2, 1, 3, 1, 2, 4, 1, 2, 1, 3, 2, 1, 4, 1, 2, 1, 3, 1, 2, 2, 1, 3, 1, 4, 1, 2, 1, 2, 3, 1, 2
    )
    Canvas(modifier = modifier.height(34.dp)) {
        val totalUnits = barWeights.sum() + barWeights.size
        val unitWidth = size.width / totalUnits.toFloat()
        var x = 0f
        barWeights.forEachIndexed { index, w ->
            val barW = w * unitWidth
            if (index % 2 == 0) {
                drawRect(
                    color = Color(0xFF2B241D),
                    topLeft = Offset(x, 0f),
                    size = Size(barW, size.height)
                )
            }
            x += barW + unitWidth * 0.65f
        }
    }
}

@Composable
fun QrisMatrixGraphic(
    modifier: Modifier = Modifier
) {
    val matrix = listOf(
        "111110101011111",
        "100010110010001",
        "101010011010101",
        "101010101010101",
        "100010110010001",
        "111110101011111",
        "000000101000000",
        "101101101101010",
        "010110010110101",
        "000000101101011",
        "111110110010110",
        "100010011011001",
        "101010101001110",
        "100010110010101",
        "111110101101011"
    )
    Canvas(modifier = modifier.size(104.dp)) {
        val n = matrix.size
        val cell = size.width / n
        matrix.forEachIndexed { r, row ->
            row.forEachIndexed { c, ch ->
                if (ch == '1') {
                    drawRect(
                        color = Color(0xFF221D18),
                        topLeft = Offset(c * cell, r * cell),
                        size = Size(cell, cell)
                    )
                }
            }
        }
    }
}

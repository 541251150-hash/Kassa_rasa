package com.kassarasa.pos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kassarasa.pos.navigation.AppRoute
import com.kassarasa.pos.theme.NavActiveGold
import com.kassarasa.pos.theme.NavActiveTile
import com.kassarasa.pos.theme.NavInactiveSage
import com.kassarasa.pos.theme.OliveDark
import com.kassarasa.pos.theme.OliveNavBar
import com.kassarasa.pos.theme.OlivePosBar

data class BottomNavEntry(
    val label: String,
    val route: AppRoute,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun DashboardBottomBar(
    currentRoute: AppRoute,
    onNavigate: (AppRoute) -> Unit
) {
    val entries = listOf(
        BottomNavEntry("Dashboard", AppRoute.Dashboard, Icons.Rounded.GridView, Icons.Outlined.GridView),
        BottomNavEntry("Menu", AppRoute.KelolaMenu, Icons.AutoMirrored.Rounded.MenuBook, Icons.Outlined.RestaurantMenu),
        BottomNavEntry("Transaksi", AppRoute.Transaksi, Icons.AutoMirrored.Rounded.ReceiptLong, Icons.AutoMirrored.Outlined.ReceiptLong),
        BottomNavEntry("Riwayat", AppRoute.Riwayat, Icons.Rounded.History, Icons.Outlined.History),
        BottomNavEntry("Laporan", AppRoute.Laporan, Icons.Rounded.Assessment, Icons.Outlined.Assessment)
    )
    OliveBottomBarContainer(
        backgroundColor = OliveDark,
        entries = entries,
        currentRoute = currentRoute,
        useTileHighlight = true,
        showActiveDot = false,
        onNavigate = onNavigate
    )
}

@Composable
fun KelolaMenuBottomBar(
    currentRoute: AppRoute,
    onNavigate: (AppRoute) -> Unit
) {
    val entries = listOf(
        BottomNavEntry("Beranda", AppRoute.Dashboard, Icons.Rounded.GridView, Icons.Outlined.GridView),
        BottomNavEntry("Kasir", AppRoute.Transaksi, Icons.AutoMirrored.Rounded.ReceiptLong, Icons.AutoMirrored.Outlined.ReceiptLong),
        BottomNavEntry("Kelola Menu", AppRoute.KelolaMenu, Icons.AutoMirrored.Rounded.MenuBook, Icons.AutoMirrored.Outlined.MenuBook),
        BottomNavEntry("Riwayat", AppRoute.Riwayat, Icons.Rounded.History, Icons.Outlined.History),
        BottomNavEntry("Laporan", AppRoute.Laporan, Icons.Rounded.Assessment, Icons.Outlined.Assessment)
    )
    OliveBottomBarContainer(
        backgroundColor = OliveNavBar,
        entries = entries,
        currentRoute = currentRoute,
        useTileHighlight = false,
        showActiveDot = true,
        onNavigate = onNavigate
    )
}

@Composable
fun RiwayatBottomBar(
    currentRoute: AppRoute,
    onNavigate: (AppRoute) -> Unit
) {
    val entries = listOf(
        BottomNavEntry("Kasir", AppRoute.Transaksi, Icons.AutoMirrored.Rounded.ReceiptLong, Icons.Outlined.Home),
        BottomNavEntry("Menu", AppRoute.KelolaMenu, Icons.AutoMirrored.Rounded.MenuBook, Icons.AutoMirrored.Outlined.ReceiptLong),
        BottomNavEntry("Riwayat", AppRoute.Riwayat, Icons.Rounded.History, Icons.Outlined.History),
        BottomNavEntry("Laporan", AppRoute.Laporan, Icons.Rounded.Assessment, Icons.Outlined.Assessment)
    )
    OliveBottomBarContainer(
        backgroundColor = OlivePosBar,
        entries = entries,
        currentRoute = currentRoute,
        useTileHighlight = false,
        showActiveDot = false,
        onNavigate = onNavigate
    )
}

@Composable
private fun OliveBottomBarContainer(
    backgroundColor: Color,
    entries: List<BottomNavEntry>,
    currentRoute: AppRoute,
    useTileHighlight: Boolean,
    showActiveDot: Boolean,
    onNavigate: (AppRoute) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        entries.forEach { entry ->
            val selected = entry.route == currentRoute
            val interactionSource = remember { MutableInteractionSource() }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (!selected) onNavigate(entry.route)
                    }
                    .padding(vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (selected && useTileHighlight) {
                                Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NavActiveTile)
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            } else {
                                Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selected) entry.activeIcon else entry.inactiveIcon,
                        contentDescription = entry.label,
                        tint = if (selected) NavActiveGold else NavInactiveSage,
                        modifier = Modifier.size(22.dp)
                    )
                    if (selected && showActiveDot) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-2).dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NavActiveGold)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = entry.label,
                    color = when {
                        selected && !useTileHighlight -> NavActiveGold
                        selected -> Color.White
                        else -> NavInactiveSage
                    },
                    fontSize = 11.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

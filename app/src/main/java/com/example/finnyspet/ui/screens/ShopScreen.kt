package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.finnyspet.data.Content
import com.example.finnyspet.data.ShopItem
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun ShopScreen(vm: GameViewModel, tab: String, onBack: () -> Unit) {
    var currentTab by remember { mutableStateOf(tab) }
    val state by vm.state

    val items = when (currentTab) {
        "food" -> Content.shopFood
        "play" -> Content.shopPlay
        else   -> Content.shopCare
    }
    val title = when (currentTab) {
        "food" -> "🍽️ Еда"
        "play" -> "🎮 Игры"
        else   -> "🧼 Уход"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Card2)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextMain, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Text(title, color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text(
                "🟢 ${state.walletSpend}",
                color = Green,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Card2, RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TabChip("🍽️ Еда", currentTab == "food") { currentTab = "food" }
            TabChip("🎮 Игры", currentTab == "play") { currentTab = "play" }
            TabChip("🧼 Уход", currentTab == "care") { currentTab = "care" }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(items) { item ->
                ShopRow(item, canAfford = state.walletSpend >= item.price) {
                    // Покупка + возврат на главный экран
                    if (state.walletSpend >= item.price) {
                        vm.buyItem(item.id, currentTab)
                        onBack()
                    }
                }
            }
        }
    }
}

@Composable
private fun TabChip(text: String, active: Boolean, onClick: () -> Unit) {
    Text(
        text,
        color = if (active) TextMain else Muted,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .heightIn(min = 52.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(if (active) Purple else Card2)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    )
}

@Composable
private fun ShopRow(
    item: ShopItem,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Card2)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(item.emoji, fontSize = 40.sp, modifier = Modifier.width(52.dp))
        Column(Modifier.weight(1f)) {
            Text(item.name, color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(item.desc, color = Muted, fontSize = 12.sp)
        }
        Text(
            if (item.price == 0) "Бесплатно" else "${item.price} 🪙",
            color = TextMain,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .heightIn(min = 52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (canAfford) Green else Card3)
                .clickable(enabled = canAfford) { onBuy() }
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}
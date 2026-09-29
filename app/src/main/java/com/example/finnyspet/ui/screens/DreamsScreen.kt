package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finnyspet.data.Content
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun DreamsScreen(vm: GameViewModel, onBack: () -> Unit) {
    val state by vm.state
    val goal = state.walletGoal

    val dream = Content.dreams.find { it.id == "house" } ?: Content.dreams.first()
    val owned = vm.isDreamOwned(dream.id)
    val progress = if (dream.price > 0) (goal.toFloat() / dream.price).coerceIn(0f, 1f) else 0f
    val remaining = (dream.price - goal).coerceAtLeast(0)
    val daysLeft = if (remaining > 0) (remaining + 19) / 20 else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
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
            Text("🎯 Мечты питомца", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))

        // Красивая карточка мечты
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6D28D9), Color(0xFFA855F7), Color(0xFFEC4899))
                    )
                )
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2A1A4A), Color(0xFF1A1030))
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0x66FBBF24), Color(0x00FBBF24))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (owned) "🏡✨" else "🏡",
                        fontSize = 110.sp
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    dream.name,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    dream.bonus,
                    color = Color(0xFFFBBF24),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(18.dp))

                if (owned) {
                    Text(
                        "✅ Мечта сбылась!",
                        color = Green,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Накоплено", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                        Spacer(Modifier.weight(1f))
                        Text(
                            "$goal / ${dream.price} 🪙",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFF151530))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
                                    )
                                )
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${(progress * 100).toInt()}%",
                            color = Color(0xFFFBBF24),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "Осталось $remaining 🪙",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }

                    if (remaining > 0) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "≈ $daysLeft дней при пополнении 20/день",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (owned) {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Card2)
            ) {
                Text("← Назад к питомцу", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
            }
        } else {
            Button(
                onClick = { vm.buyDream(dream.id) },
                enabled = goal >= dream.price,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    disabledContainerColor = Card3
                )
            ) {
                Text(
                    if (goal >= dream.price) "🏡 Купить ${dream.name}"
                    else "Не хватает ${dream.price - goal} 🪙",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))

            if (state.walletSpend > 0) {
                Text(
                    "В «Тратах»: ${state.walletSpend} 🪙",
                    color = Muted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAddBtn("+10", Modifier.weight(1f)) { vm.addToGoal(10) }
                    QuickAddBtn("+20", Modifier.weight(1f)) { vm.addToGoal(20) }
                    QuickAddBtn(
                        "+${minOf(50, state.walletSpend)}",
                        Modifier.weight(1f)
                    ) { vm.addToGoal(minOf(50, state.walletSpend)) }
                }
            } else {
                Text(
                    "В «Тратах» пусто. Заработай на заданиях!",
                    color = Muted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "← Назад к питомцу",
                color = Muted,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Card2)
                    .clickable { onBack() }
                    .padding(vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun QuickAddBtn(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text,
        color = TextMain,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Card3)
            .clickable { onClick() }
            .padding(vertical = 16.dp)
    )
}
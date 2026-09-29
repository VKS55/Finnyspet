package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun WalletScreen(vm: GameViewModel, onBack: () -> Unit) {
    val state by vm.state
    val total = state.unallocated

    var distributing by remember { mutableStateOf(false) }
    var spend by remember { mutableStateOf(0f) }
    var goal by remember { mutableStateOf(0f) }
    var cushion by remember { mutableStateOf(0f) }

    // 🔒 Слайдеры инициализируются ОДИН РАЗ при входе на экран.
    // Ключ `Unit` не перезапускает эффект при изменении total.
    var initialized by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!initialized && total > 0) {
            spend = total * 0.6f
            goal = total * 0.3f
            cushion = total * 0.1f
            initialized = true
        }
    }

    val used = spend + goal + cushion
    val left = total - used
    val canDistribute = total > 0 && left == 0f && !distributing

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
            Text("🏦 Копилка", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WalletChip("🟢 Траты", state.walletSpend, Green, Modifier.weight(1f))
            WalletChip("🟡 Цели", state.walletGoal, Yellow, Modifier.weight(1f))
            WalletChip("🔵 Подушка", state.walletCushion, Blue, Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        val (hintText, hintColor) = when {
            total == 0 -> "Свободных монет нет" to Muted
            left > 0f -> "Осталось разложить: ${left.toInt()} 🪙" to Yellow
            left < 0f -> "Лишних: ${(-left).toInt()} 🪙" to Red
            else -> "Всё распределено ✅" to Green
        }

        Text(hintText, color = hintColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (total == 0) {
            Text(
                "Сейчас нечего распределять. Заработай на заданиях!",
                color = Muted,
                fontSize = 15.sp
            )
        } else {
            SliderRow("🟢 Траты", spend.toInt(), total) { spend = it.toFloat() }
            SliderRow("🟡 Цели", goal.toInt(), total) { goal = it.toFloat() }
            SliderRow("🔵 Подушка", cushion.toInt(), total) { cushion = it.toFloat() }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickBtn("60/30/10", Modifier.weight(1f)) {
                    spend = total * 0.6f; goal = total * 0.3f; cushion = total * 0.1f
                }
                QuickBtn("70/20/10", Modifier.weight(1f)) {
                    spend = total * 0.7f; goal = total * 0.2f; cushion = total * 0.1f
                }
                QuickBtn("50/40/10", Modifier.weight(1f)) {
                    spend = total * 0.5f; goal = total * 0.4f; cushion = total * 0.1f
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!canDistribute) return@Button
                    distributing = true
                    vm.distribute(spend.toInt(), goal.toInt(), cushion.toInt())
                    // 🔒 Сразу возвращаемся на главный — не даём нажать второй раз
                    onBack()
                },
                enabled = canDistribute,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple,
                    disabledContainerColor = Card3
                )
            ) {
                Text("Разложить", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "← Вернуться на главный",
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

@Composable
private fun WalletChip(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Card2)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = Muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(2.dp))
        Text("$value", color = color, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SliderRow(label: String, value: Int, max: Int, onChange: (Int) -> Unit) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(label, color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            Text("$value", color = Yellow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.toInt()) },
            valueRange = 0f..max.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Purple,
                activeTrackColor = Purple,
                inactiveTrackColor = Card3
            )
        )
    }
}

@Composable
private fun QuickBtn(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text,
        color = TextMain,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Card3)
            .clickable { onClick() }
            .padding(vertical = 16.dp)
    )
}
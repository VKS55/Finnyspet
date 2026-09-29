package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.finnyspet.data.Content
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun MainScreen(
    vm: GameViewModel,
    onShop: (String) -> Unit,
    onTasks: () -> Unit,
    onDreams: () -> Unit,
    onWallet: () -> Unit,
    onAdult: () -> Unit
) {
    val state by vm.state
    val sova = vm.sovaMessage.value
    val pet = Content.pets.find { it.id == state.petId } ?: Content.pets[0]

    val totalCoins = state.walletSpend + state.walletGoal + state.walletCushion + state.unallocated

    var showCushionDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Bg)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "🪙 $totalCoins",
                    color = Yellow,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Card2, RoundedCornerShape(999.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "⭐ Ур. ${state.progress / 100 + 1}",
                    color = TextMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(Purple, RoundedCornerShape(999.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2D2D54), Color(0xFF1F1F40))
                        )
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    pet.emoji,
                    fontSize = 80.sp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { vm.petTap() }
                        .padding(16.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    state.petName,
                    color = Muted,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            StatRow("🍎", state.food, Color(0xFFFB923C))
            StatRow("😊", state.mood, Color(0xFFEC4899))
            StatRow("⚡", state.energy, Color(0xFF3B82F6))
            StatRow("✨", state.health, Color(0xFF22C55E))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WalletBox("🟢 Траты", state.walletSpend, Green, Modifier.weight(1f)) {
                    onShop("food")
                }
                WalletBox("🟡 Цели", state.walletGoal, Yellow, Modifier.weight(1f)) {
                    onDreams()
                }
                WalletBox("🔵 Подушка", state.walletCushion, Blue, Modifier.weight(1f)) {
                    showCushionDialog = true
                }
            }

            Spacer(Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                NavBtn("🍽️", "Еда", Modifier.weight(1f)) { onShop("food") }
                NavBtn("🎮", "Игры", Modifier.weight(1f)) { onShop("play") }
                NavBtn("🧼", "Уход", Modifier.weight(1f)) { onShop("care") }
                NavBtn("📋", "Задания", Modifier.weight(1f)) { onTasks() }
                NavBtn("🏦", "Копилка", Modifier.weight(1f)) { onWallet() }
                NavBtn("👪", "Взрослый", Modifier.weight(1f)) { onAdult() }
            }
        }

        if (sova != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF4C1D95))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🦉", fontSize = 24.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    sova,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (showCushionDialog) {
            CushionDialog(
                cushion = state.walletCushion,
                onTake = { amount -> vm.withdrawFromCushion(amount) },
                onClose = { showCushionDialog = false }
            )
        }
    }
}

@Composable
private fun StatRow(label: String, value: Float, color: Color) {
    val v = value.coerceIn(0f, 100f)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 18.sp, modifier = Modifier.width(30.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(16.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0xFF151530))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(v / 100f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(color)
            )
        }
        Text(
            "%.1f".format(v),
            fontSize = 13.sp,
            color = if (v < 30f) Color(0xFFF87171) else Muted,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(48.dp)
        )
    }
}

@Composable
private fun WalletBox(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .heightIn(min = 64.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Card2)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, color = Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(2.dp))
        Text("$value", color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun NavBtn(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .heightIn(min = 68.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Card2)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 26.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            label,
            color = Muted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun CushionDialog(
    cushion: Int,
    onTake: (Int) -> Unit,
    onClose: () -> Unit
) {
    var input by remember { mutableStateOf("") }

    val amount = input.toIntOrNull() ?: 0
    val isValid = amount in 1..cushion

    Dialog(onDismissRequest = onClose) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Card2)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🔵 Подушка", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text(
                "В подушке: $cushion 🪙",
                color = Blue,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            if (cushion <= 0) {
                Text("Подушка пуста", color = Muted, fontSize = 15.sp)
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Card3)
                ) {
                    Text("Закрыть", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextMain)
                }
                return@Column
            }

            Text(
                "Сколько взять?",
                color = Muted,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { new ->
                    if (new.length <= 6 && new.all { it.isDigit() }) input = new
                },
                placeholder = { Text("Например, 10") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp)
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickAddBtn("+10", Modifier.weight(1f)) {
                    val cur = input.toIntOrNull() ?: 0
                    input = (cur + 10).coerceAtMost(cushion).toString()
                }
                QuickAddBtn("+50", Modifier.weight(1f)) {
                    val cur = input.toIntOrNull() ?: 0
                    input = (cur + 50).coerceAtMost(cushion).toString()
                }
                QuickAddBtn("Всё", Modifier.weight(1f)) {
                    input = cushion.toString()
                }
            }

            Spacer(Modifier.height(8.dp))

            if (input.isNotEmpty() && !isValid) {
                Text(
                    "Введи число от 1 до $cushion",
                    color = Red,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (isValid) {
                        onTake(amount)
                        input = ""
                    }
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                Text(
                    if (isValid) "Взять $amount 🪙" else "Введи число",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Card3)
            ) {
                Text("Закрыть", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextMain)
            }
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
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Card3)
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    )
}
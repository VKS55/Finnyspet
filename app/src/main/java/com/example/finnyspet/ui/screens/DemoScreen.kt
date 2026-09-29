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
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun DemoScreen(vm: GameViewModel, onBack: () -> Unit) {
    val state by vm.state

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
            Text("🎬 Демо-режим", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF4C1D95), Color(0xFF6D28D9))
                    )
                )
                .padding(16.dp)
        ) {
            Text("🎬 Для экспертов", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Этот режим позволяет пройти обязательный сценарий без ожидания календарных сроков.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusChip(
                "🪙 ${state.walletSpend + state.walletGoal + state.walletCushion + state.unallocated}",
                Yellow,
                Modifier.weight(1f)
            )
            StatusChip("📅 День ${state.streak}", Purple, Modifier.weight(1f))
            StatusChip(
                "📋 ${if (state.demoShowAllTasks) "Все" else "Обычно"}",
                Green,
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        DemoButton(
            emoji = "⏩",
            title = "Прокрутить день вперёд",
            subtitle = "Эмулирует новый день и начисляет доход",
            color = Purple
        ) { vm.demoTickDay() }

        Spacer(Modifier.height(10.dp))

        DemoButton(
            emoji = "⏰",
            title = "Прокрутить час",
            subtitle = "Обновляет почасовые задания",
            color = Blue
        ) { vm.demoTickHour() }

        Spacer(Modifier.height(10.dp))

        DemoButton(
            emoji = "📋",
            title = "Показать все задания",
            subtitle = "Открывает доступ ко всем 21 заданию сразу",
            color = Green
        ) { vm.demoShowAllTasks() }

        Spacer(Modifier.height(10.dp))

        DemoButton(
            emoji = "🗑",
            title = "Сбросить тестовый профиль",
            subtitle = "Возвращает игру в исходное состояние",
            color = Color(0xFFD97706)
        ) {
            vm.demoReset()
            onBack()
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF4C1D95))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🦉", fontSize = 22.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                "Используйте эти кнопки, чтобы показать все этапы игры за минуту.",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun DemoButton(
    emoji: String,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 30.sp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    subtitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        color = color,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Card2)
            .padding(vertical = 10.dp),
        textAlign = TextAlign.Center
    )
}
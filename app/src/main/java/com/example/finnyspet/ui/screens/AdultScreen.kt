package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun AdultScreen(
    vm: GameViewModel,
    onBack: () -> Unit,
    onOpenDemo: () -> Unit
) {
    val state by vm.state

    var stage by remember { mutableStateOf(if (vm.adultHash() == null) "setup" else "login") }
    var p1 by remember { mutableStateOf("") }
    var p2 by remember { mutableStateOf("") }
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

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
            Text("👨‍👩‍👧 Для взрослого", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))

        when (stage) {
            "setup" -> {
                Text("Придумайте пароль (4–8 цифр)", color = Muted, fontSize = 15.sp)
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = p1,
                    onValueChange = { if (it.length <= 8 && it.all { c -> c.isDigit() }) p1 = it },
                    label = { Text("Новый пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 6.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = p2,
                    onValueChange = { if (it.length <= 8 && it.all { c -> c.isDigit() }) p2 = it },
                    label = { Text("Повторите") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 6.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                )
                Spacer(Modifier.height(14.dp))

                if (error.isNotEmpty()) {
                    Text(error, color = Red, fontSize = 14.sp)
                    Spacer(Modifier.height(10.dp))
                }

                Button(
                    onClick = {
                        if (p1.length < 4) { error = "Пароль — от 4 до 8 цифр"; return@Button }
                        if (p1 != p2) { error = "Пароли не совпадают"; return@Button }
                        vm.saveAdultPassword(p1)
                        error = ""
                        stage = "content"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text("Сохранить пароль", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            "login" -> {
                Text("Введите пароль, чтобы войти", color = Muted, fontSize = 15.sp)
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = input,
                    onValueChange = { if (it.length <= 8 && it.all { c -> c.isDigit() }) input = it },
                    label = { Text("Пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 8.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                )
                Spacer(Modifier.height(14.dp))

                if (error.isNotEmpty()) {
                    Text(error, color = Red, fontSize = 14.sp)
                    Spacer(Modifier.height(10.dp))
                }

                Button(
                    onClick = {
                        if (vm.checkPassword(input)) {
                            error = ""
                            stage = "content"
                        } else {
                            error = "Неверный пароль"
                            input = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text("Войти", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            "content" -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CardRow("Питомец", state.petName)
                    CardRow(
                        "Всего монет",
                        "${state.walletSpend + state.walletGoal + state.walletCushion + state.unallocated} 🪙"
                    )
                    CardRow("В «Целях»", "${state.walletGoal} 🪙")
                    CardRow("В «Подушке»", "${state.walletCushion} 🪙")
                    CardRow("Прогресс", "${state.progress}")
                    CardRow("Заданий выполнено", "${state.weekTasks}")
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        vm.resetOnboarding()
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Card2)
                ) {
                    Text("📖 Показать обучение", color = TextMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = onOpenDemo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text("🎬 Демо-режим", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        vm.clearAdultPassword()
                        stage = "setup"
                        p1 = ""; p2 = ""; input = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Card2)
                ) {
                    Text("🔑 Сменить пароль", color = TextMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        vm.resetAll()
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text("🗑 Сбросить профиль", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CardRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Card2)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Muted, fontSize = 15.sp, modifier = Modifier.weight(1f))
        Text(value, color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}
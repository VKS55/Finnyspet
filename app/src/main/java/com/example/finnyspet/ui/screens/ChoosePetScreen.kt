package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.finnyspet.data.Content
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun ChoosePetScreen(vm: GameViewModel, onStart: () -> Unit) {
    var selectedId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(16.dp)
    ) {
        Text(
            "🦉",
            fontSize = 52.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            "Привет! Я — Мудрая Сова",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Помогу вырастить питомца и управлять монетками",
            color = Muted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(18.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(Content.pets, key = { it.id }) { pet ->
                val isSelected = selectedId == pet.id
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Card3 else Card2)
                        .border(
                            width = 3.dp,
                            color = if (isSelected) Purple else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedId = pet.id }
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(pet.emoji, fontSize = 60.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        pet.name,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        fontSize = 16.sp
                    )
                    Text(
                        pet.desc,
                        color = Muted,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        pet.bonus,
                        color = Yellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val id = selectedId ?: return@Button
                vm.startGame(id)
                onStart()
            },
            enabled = selectedId != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(
                text = if (selectedId == null) "Выбери питомца" else "Начать!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
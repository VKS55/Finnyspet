package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finnyspet.data.Task
import com.example.finnyspet.data.TaskType
import com.example.finnyspet.ui.theme.*
import com.example.finnyspet.vm.GameViewModel

@Composable
fun TasksScreen(vm: GameViewModel, onBack: () -> Unit) {
    var activeTask by remember { mutableStateOf<Task?>(null) }
    var chosenIndex by remember { mutableStateOf<Int?>(null) }
    var countInput by remember { mutableStateOf("") }
    var countFeedback by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

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
            Text("📋 Задания", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(14.dp))

        if (activeTask == null) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                item { SectionHeader("⏰ Почасовые (3 из пула)", Color(0xFF3B82F6)) }
                items(vm.getActiveHourlyTasks()) { task ->
                    TaskCard(task, vm.isTaskDone(task.id)) {
                        activeTask = task
                        chosenIndex = null
                        countInput = ""
                        countFeedback = null
                    }
                }

                item { Spacer(Modifier.height(10.dp)) }

                item { SectionHeader("🌞 Ежедневные (2 на день)", Purple) }
                items(vm.getActiveDailyTasks()) { task ->
                    TaskCard(task, vm.isTaskDone(task.id)) {
                        activeTask = task
                        chosenIndex = null
                        countInput = ""
                        countFeedback = null
                    }
                }

                item { Spacer(Modifier.height(10.dp)) }

                item { SectionHeader("📅 Еженедельные (1 на неделю)", Color(0xFF16A34A)) }
                items(vm.getActiveWeeklyTasks()) { task ->
                    TaskCard(task, vm.isTaskDone(task.id)) {
                        activeTask = task
                        chosenIndex = null
                        countInput = ""
                        countFeedback = null
                    }
                }
            }
        } else {
            val task = activeTask!!
            Column(Modifier.fillMaxSize()) {
                Text(task.theme, color = Yellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(task.title, color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(task.desc, color = Muted, fontSize = 15.sp)
                Spacer(Modifier.height(18.dp))

                val alreadyDone = vm.isTaskDone(task.id)

                when (task.type) {
                    TaskType.CHOICE, TaskType.YESNO -> {
                        task.choices.forEachIndexed { index, choice ->
                            val chosen = chosenIndex
                            val bg = when {
                                chosen == null -> Card2
                                index != chosen -> Card2
                                choice.correct -> Color(0xFF14532D)
                                else -> Color(0xFF7F1D1D)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 60.dp)
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(bg)
                                    .clickable(enabled = chosen == null && !alreadyDone) {
                                        chosenIndex = index
                                        if (choice.correct) {
                                            val reward = choice.reward ?: task.reward
                                            vm.completeTask(task.id, reward)
                                        }
                                    }
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(choice.text, color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                if (chosen == index) {
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        (if (choice.correct) "✅ " else "💡 ") + choice.feedback,
                                        color = if (choice.correct) Green else Yellow,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        if (alreadyDone) {
                            Spacer(Modifier.height(14.dp))
                            Text("✅ Это задание уже выполнено!", color = Green, fontSize = 15.sp)
                        }
                    }

                    TaskType.COUNT -> {
                        Text("Введи число:", color = Muted, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = countInput,
                            onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) countInput = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            enabled = !alreadyDone,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 64.dp)
                        )
                        Spacer(Modifier.height(12.dp))

                        countFeedback?.let { (ok, msg) ->
                            Text(
                                (if (ok) "✅ " else "💡 ") + msg,
                                color = if (ok) Green else Yellow,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        if (alreadyDone) {
                            Text("✅ Это задание уже выполнено!", color = Green, fontSize = 15.sp)
                        } else {
                            Button(
                                onClick = {
                                    val v = countInput.toIntOrNull()
                                    if (v == null) {
                                        countFeedback = false to "Введи число"
                                    } else if (v == task.answer) {
                                        countFeedback = true to "Верно! +${task.reward} 🪙"
                                        vm.completeTask(task.id, task.reward)
                                    } else {
                                        countFeedback = false to "Правильный ответ: ${task.answer}"
                                    }
                                },
                                enabled = countFeedback?.first != true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Purple)
                            ) {
                                Text("Проверить", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Text(
                    "← К списку заданий",
                    color = TextMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 68.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Card2)
                        .clickable {
                            activeTask = null
                            chosenIndex = null
                            countInput = ""
                            countFeedback = null
                        }
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String, color: Color) {
    Text(
        text,
        color = TextMain,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    )
}

@Composable
private fun TaskCard(task: Task, done: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 90.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Card2)
            .clickable(enabled = !done) { onClick() }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                task.title,
                color = TextMain,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                if (done) "✅" else "⏳",
                color = if (done) Green else Yellow,
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(task.desc, color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Награда: +${task.reward} 🪙", color = Yellow, fontSize = 13.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                when (task.type) {
                    TaskType.CHOICE -> "выбор"
                    TaskType.YESNO -> "да/нет"
                    TaskType.COUNT -> "счёт"
                },
                color = Muted,
                fontSize = 11.sp
            )
        }
    }
}
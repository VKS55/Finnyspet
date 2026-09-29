package com.example.finnyspet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var page by remember { mutableStateOf(0) }
    val totalPages = 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            "Шаг ${page + 1} из $totalPages",
            color = Muted,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        when (page) {
            0 -> SlideWelcome()
            1 -> SlideThreeDecisions()
            2 -> SlideScreenMap()
            3 -> SlideActions()
            4 -> SlideHowToPlay()
        }

        Spacer(Modifier.height(24.dp))

        // Точки-индикаторы
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(totalPages) { i ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(if (i == page) 12.dp else 10.dp)
                        .clip(CircleShape)
                        .background(if (i == page) Purple else Card3)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Кнопка «Дальше / Начать»
        Button(
            onClick = {
                if (page == totalPages - 1) onFinish() else page += 1
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(
                if (page == totalPages - 1) "Начать! 🐾" else "Дальше →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Кнопка «Назад»
        if (page > 0) {
            Spacer(Modifier.height(10.dp))
            Text(
                "← Назад",
                color = Muted,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .heightIn(min = 52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { page -= 1 }
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }

        // Кнопка «Пропустить»
        if (page < totalPages - 1) {
            Spacer(Modifier.height(6.dp))
            Text(
                "Пропустить обучение",
                color = Muted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onFinish() }
                    .padding(horizontal = 24.dp, vertical = 14.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

/* ============================================================
   СЛАЙД 1 — Приветствие
============================================================ */
@Composable
private fun SlideWelcome() {
    BigEmoji("🦉")

    Spacer(Modifier.height(20.dp))

    Text(
        "Привет! Я — Мудрая Сова",
        color = TextMain,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(16.dp))

    Text(
        "Я помогу тебе вырастить питомца и научиться управлять монетками.\n\nЭто не скучные правила — это игра, где ты сам решаешь, что делать.",
        color = TextMain,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(16.dp))

    HintBox("Готов? Тогда идём дальше — покажу, как всё устроено!")
}

/* ============================================================
   СЛАЙД 2 — Три вида решений
============================================================ */
@Composable
private fun SlideThreeDecisions() {
    BigEmoji("🍎")

    Spacer(Modifier.height(20.dp))

    Text(
        "Три вида решений",
        color = TextMain,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(16.dp))

    DecisionRow("🍎", "Обязательное", "Еда, уход, лекарства. Без них питомцу плохо.", Green)
    Spacer(Modifier.height(10.dp))
    DecisionRow("🎀", "Желаемое", "Игрушки и радости. Покупаем, если хватает денег.", Yellow)
    Spacer(Modifier.height(10.dp))
    DecisionRow("🏦", "Накопления", "Копилка на большую мечту.", Blue)

    Spacer(Modifier.height(16.dp))

    HintBox("Важно: сначала обязательное, потом желаемое. Копилка — всегда понемногу.")
}

/* ============================================================
   СЛАЙД 3 — Карта экрана
============================================================ */
@Composable
private fun SlideScreenMap() {
    BigEmoji("🗺️")

    Spacer(Modifier.height(20.dp))

    Text(
        "Как выглядит игра",
        color = TextMain,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(6.dp))
    Text(
        "Вот главный экран. Каждая кнопка делает своё дело.",
        color = Muted,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Card2)
            .padding(12.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            LabelTag("🪙 80", Yellow)
            Spacer(Modifier.weight(1f))
            LabelTag("⭐ Ур. 1", Purple)
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF2D2D54), Color(0xFF1F1F40))))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🐉", fontSize = 40.sp)
            Spacer(Modifier.height(2.dp))
            Text("← это питомец", color = Yellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🍎", fontSize = 16.sp)
            Spacer(Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF151530))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFFB923C))
                )
            }
            Spacer(Modifier.width(6.dp))
            Text("80", color = Muted, fontSize = 11.sp)
        }

        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("😊", fontSize = 16.sp)
            Spacer(Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF151530))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFEC4899))
                )
            }
            Spacer(Modifier.width(6.dp))
            Text("90", color = Muted, fontSize = 11.sp)
        }

        Spacer(Modifier.height(6.dp))
        Text("← это шкалы: еда, настроение, энергия, здоровье", color = Yellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            MiniBox("🟢 Траты", Green, Modifier.weight(1f))
            MiniBox("🟡 Цели", Yellow, Modifier.weight(1f))
            MiniBox("🔵 Подушка", Blue, Modifier.weight(1f))
        }

        Spacer(Modifier.height(4.dp))
        Text("← три кошелька: траты, цели, подушка", color = Yellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
            MiniNavBtn("🍽️", "Еда", Modifier.weight(1f))
            MiniNavBtn("🎮", "Игры", Modifier.weight(1f))
            MiniNavBtn("🧼", "Уход", Modifier.weight(1f))
            MiniNavBtn("📋", "Задания", Modifier.weight(1f))
            MiniNavBtn("🏦", "Копилка", Modifier.weight(1f))
            MiniNavBtn("👪", "Взрослый", Modifier.weight(1f))
        }

        Spacer(Modifier.height(4.dp))
        Text("← нижние кнопки: тут всё основное", color = Yellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }

    Spacer(Modifier.height(16.dp))

    HintBox("Попробуй нажать на каждую кнопку — увидишь, что она делает.")
}

/* ============================================================
   СЛАЙД 4 — Что делают кнопки
============================================================ */
@Composable
private fun SlideActions() {
    BigEmoji("👆")

    Spacer(Modifier.height(20.dp))

    Text(
        "Что где нажать?",
        color = TextMain,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(16.dp))

    ActionRow("🍽️", "Еда", "Покормить питомца, купить яблоко, кашу или суп.")
    Spacer(Modifier.height(10.dp))
    ActionRow("🎮", "Игры", "Поиграть с питомцем, чтобы поднять настроение.")
    Spacer(Modifier.height(10.dp))
    ActionRow("🧼", "Уход", "Помыть, дать витамины или показать врачу.")
    Spacer(Modifier.height(10.dp))
    ActionRow("📋", "Задания", "Выполнить задания Совы и заработать монетки.")
    Spacer(Modifier.height(10.dp))
    ActionRow("🏦", "Копилка", "Распределить доход по трём кошелькам.")
    Spacer(Modifier.height(10.dp))
    ActionRow("👪", "Взрослый", "Тут настройки для мамы и папы (нужен пароль).")

    Spacer(Modifier.height(16.dp))

    HintBox("Кроме кнопок можно нажать на самого питомца — он обрадуется!")
}

/* ============================================================
   СЛАЙД 5 — Как играть
============================================================ */
@Composable
private fun SlideHowToPlay() {
    BigEmoji("🎯")

    Spacer(Modifier.height(20.dp))

    Text(
        "Как играть?",
        color = TextMain,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(16.dp))

    StepRow("1", "Покорми и помой питомца", "Иначе он загрустит.")
    Spacer(Modifier.height(10.dp))
    StepRow("2", "Выполняй задания Совы", "Получай монетки за знания.")
    Spacer(Modifier.height(10.dp))
    StepRow("3", "Откладывай на мечту", "Немного каждый день — и цель ближе.")
    Spacer(Modifier.height(10.dp))
    StepRow("4", "Возвращайся каждый день", "За серию входов — бонусы.")

    Spacer(Modifier.height(16.dp))

    HintBox("Удачи! Я всегда рядом и подскажу, если что.")
}

/* ============================================================
   КОМПОНЕНТЫ
============================================================ */

@Composable
private fun BigEmoji(emoji: String) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(Card2),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 84.sp)
    }
}

@Composable
private fun HintBox(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF4C1D95))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("💡", fontSize = 22.sp)
        Spacer(Modifier.width(10.dp))
        Text(
            text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DecisionRow(emoji: String, title: String, desc: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Card2)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 34.sp, modifier = Modifier.width(50.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun ActionRow(emoji: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Card2)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 32.sp, modifier = Modifier.width(48.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun StepRow(number: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Card2)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Purple),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun LabelTag(text: String, color: Color) {
    Text(
        text,
        color = color,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFF151530))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun MiniBox(title: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF151530))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = Muted, fontSize = 10.sp)
        Spacer(Modifier.height(2.dp))
        Text("20", color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MiniNavBtn(emoji: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF151530))
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 16.sp)
        Text(label, color = Muted, fontSize = 8.sp, maxLines = 1)
    }
}
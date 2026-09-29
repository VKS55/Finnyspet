package com.example.finnyspet.data

data class Pet(
    val id: String,
    val emoji: String,
    val name: String,
    val desc: String,
    val bonus: String,
    val bonusKey: String
)

data class ShopItem(
    val id: String,
    val emoji: String,
    val name: String,
    val price: Int,
    val food: Int = 0,
    val mood: Int = 0,
    val energy: Int = 0,
    val health: Int = 0,
    val desc: String
)

data class Dream(
    val id: String,
    val name: String,
    val price: Int,
    val emoji: String,
    val bonus: String
)

/**
 * Вариант ответа.
 * Для типов CHOICE / YESNO — обязателен.
 * Для COUNT — не используется, вместо него `answer`.
 */
data class TaskChoice(
    val text: String,
    val correct: Boolean,
    val feedback: String,
    val reward: Int? = null
)

enum class TaskType { CHOICE, COUNT, YESNO }

data class Task(
    val id: String,
    val theme: String,
    val type: TaskType = TaskType.CHOICE,
    val title: String,
    val desc: String,
    val reward: Int,
    val choices: List<TaskChoice> = emptyList(),
    /** Только для COUNT: правильное числовое значение. */
    val answer: Int? = null
)

object Content {

    val dailyIncome = 30

    /* ============================================================
       ПИТОМЦЫ
    ============================================================ */
    val pets = listOf(
        Pet("dragon",  "🐉", "Дракончик", "Любит приключения", "+10% к наградам", "taskBonus"),
        Pet("fox",     "🦊", "Лисёнок",  "Хитрый и весёлый",  "−10% на игры",    "playDiscount"),
        Pet("panda",   "🐼", "Панда",    "Мягкий и добрый",   "+5 к настроению", "dailyMood"),
        Pet("robot",   "🐱", "Робот-кот", "Умный и точный",   "+5% к процентам", "interestBonus"),
        Pet("axolotl", "🦎", "Аксолотль", "Спокойный",        "−20% к событиям", "eventShield"),
        Pet("unicorn", "🦄", "Единорог",  "Мечтательный",     "+50 монет на старте", "startMoney")
    )

    /* ============================================================
       МАГАЗИН
    ============================================================ */
    val shopFood = listOf(
        ShopItem("apple",    "🍎", "Яблоко",  5,  food = 15, desc = "+15 сытости"),
        ShopItem("porridge", "🥣", "Каша",    10, food = 30, desc = "+30 сытости"),
        ShopItem("soup",     "🍲", "Суп",     15, food = 40, health = 5, desc = "+40 сытости, +5 здоровья"),
        ShopItem("cake",     "🍰", "Тортик",  25, food = 20, mood = 15, health = -5, desc = "+20 сытости, +15 настроения"),
        ShopItem("basket",   "🧺", "Корзина", 40, food = 80, desc = "+80 сытости — выгодно!")
    )

    val shopPlay = listOf(
        ShopItem("ball", "🎾", "Мячик",   10, mood = 20, energy = -5, desc = "+20 настроения, −5 энергии"),
        ShopItem("walk", "🚶", "Прогулка",15, mood = 25, energy = 10, desc = "+25 настроения, +10 энергии"),
        ShopItem("park", "🌳", "Парк",    30, mood = 40, energy = 20, desc = "+40 настроения, +20 энергии"),
        ShopItem("movie","🎬", "Кино",    25, mood = 35, energy = -5, desc = "+35 настроения, −5 энергии")
    )

    val shopCare = listOf(
        ShopItem("bath",    "🛁", "Купание", 8,  health = 15, mood = 5, desc = "+15 здоровья, +5 настроения"),
        ShopItem("vitamins","💊", "Витамины",12, health = 25, desc = "+25 здоровья"),
        ShopItem("sleep",   "😴", "Сон",     0,  energy = 30, food = -10, desc = "+30 энергии, −10 сытости"),
        ShopItem("doctor",  "🩺", "Врач",    20, health = 40, mood = -5, desc = "+40 здоровья, −5 настроения")
    )

    val dreams = listOf(
        Dream("toy",     "Новая игрушка",  80,  "🧸", "+5 к настроению"),
        Dream("pillow",  "Уютная подушка", 120, "🛏️", "+5 к энергии"),
        Dream("house",   "Домик",          200, "🏡", "+20 к максимуму энергии"),
        Dream("scooter", "Самокат",        300, "🛴", "+50 настроения"),
        Dream("garden",  "Волшебный сад",  800, "🌳", "+5 монет в день")
    )

    /* ============================================================
       ⏰ ПОЧАСОВЫЕ ЗАДАНИЯ — 9 штук
    ============================================================ */
    val hourlyTasks = listOf(

        /* ── Тема 1: Планирование бюджета ─────────── */
        Task(
            id = "hour_budget",
            theme = "Планирование бюджета",
            title = "⏰ Бюджет на час",
            desc = "У тебя 100 монет и 4 траты по 20. Хватит ли на все?",
            reward = 20,
            choices = listOf(
                TaskChoice("Да, останется 20", true, "4×20 = 80. Хватает!"),
                TaskChoice("Нет, не хватит", false, "4×20 = 80 ≤ 100"),
                TaskChoice("Хватит только на 3", false, "Все 4 влезают в 100.")
            )
        ),
        Task(
            id = "hour_plan",
            theme = "Планирование бюджета",
            title = "📅 План на час",
            desc = "У тебя 3 дела: поесть (10), поиграть (15), поспать (0). С чего начать?",
            reward = 20,
            choices = listOf(
                TaskChoice("С еды — 10", true, "Голодный питомец ничему не рад."),
                TaskChoice("С игры — 15", false, "Сначала необходимое."),
                TaskChoice("Со сна — 0", false, "Голодным не заснёшь.")
            )
        ),
        Task(
            id = "hour_percent",
            theme = "Планирование бюджета",
            type = TaskType.COUNT,
            title = "🔢 Посчитай бюджет",
            desc = "Доход 100. Отложил 30 в копилку и 40 на обязательное. Сколько осталось на желаемое?",
            reward = 40,
            answer = 30
        ),

        /* ── Тема 2: Сбережения и цели ────────────── */
        Task(
            id = "hour_save",
            theme = "Сбережения и цели",
            title = "💾 Копилка за час",
            desc = "Ты получил 50 монет. Сколько отложить, чтобы через 2 часа было 100?",
            reward = 20,
            choices = listOf(
                TaskChoice("50", true, "50 + 50 = 100"),
                TaskChoice("25", false, "Будет 75. Нужно 50."),
                TaskChoice("10", false, "Маловато. Нужно 50.")
            )
        ),
        Task(
            id = "hour_count_days",
            theme = "Сбережения и цели",
            type = TaskType.COUNT,
            title = "🔢 Посчитай дни",
            desc = "Цель стоит 200. Откладываешь 40 в день. За сколько дней накопишь?",
            reward = 40,
            answer = 5
        ),
        Task(
            id = "hour_repeat",
            theme = "Сбережения и цели",
            type = TaskType.YESNO,
            title = "✅ Быстрый вопрос",
            desc = "Правда ли, что откладывать понемногу каждый день надёжнее, чем один раз много?",
            reward = 15,
            choices = listOf(
                TaskChoice("Да", true, "Регулярность важнее порывов."),
                TaskChoice("Нет", false, "Как раз наоборот — маленькие шаги надёжнее.")
            )
        ),

        /* ── Тема 3: Покупки и платежи ────────────── */
        Task(
            id = "hour_choice",
            theme = "Покупки и платежи",
            title = "🤔 Что важнее?",
            desc = "Питомец просит воды. Вода — 3, сок — 8, лимонад — 12. Что купить?",
            reward = 20,
            choices = listOf(
                TaskChoice("Воду — 3", true, "Утолит жажду и сэкономит 9."),
                TaskChoice("Сок — 8", false, "Вода справится за 3."),
                TaskChoice("Лимонад — 12", false, "Дорого! Вода — то же самое.")
            )
        ),
        Task(
            id = "hour_compare",
            theme = "Покупки и платежи",
            title = "🛒 Где дешевле?",
            desc = "Молоко у Совы 6, у Ежа 4. Разница?",
            reward = 20,
            choices = listOf(
                TaskChoice("2 — у Ежа дешевле", true, "6 − 4 = 2"),
                TaskChoice("4 — у Совы дешевле", false, "У Совы дороже."),
                TaskChoice("Одинаково", false, "Разница 2.")
            )
        ),
        Task(
            id = "hour_count_change",
            theme = "Покупки и платежи",
            type = TaskType.COUNT,
            title = "🔢 Посчитай сдачу",
            desc = "У тебя 50 монет. Купил еду за 30. Сколько осталось?",
            reward = 40,
            answer = 20
        ),
        Task(
            id = "hour_yesno_must",
            theme = "Покупки и платежи",
            type = TaskType.YESNO,
            title = "✅ Быстрый вопрос",
            desc = "Можно ли покупать игрушки, если ещё не куплена еда?",
            reward = 15,
            choices = listOf(
                TaskChoice("Нет, сначала еда", true, "Сначала обязательное!"),
                TaskChoice("Да, игрушки важнее", false, "Голодный питомец грустит.")
            )
        ),

        /* ── Тема 4: Доходы и заработок ───────────── */
        Task(
            id = "hour_income",
            theme = "Доходы и заработок",
            title = "💼 Откуда деньги?",
            desc = "Откуда в семье берутся деньги?",
            reward = 20,
            choices = listOf(
                TaskChoice("Взрослые зарабатывают на работе", true, "Деньги — результат работы."),
                TaskChoice("Появляются сами", false, "Нет, их зарабатывают."),
                TaskChoice("Можно попросить у любого", false, "Просить можно, но уважать труд важно.")
            )
        ),
        Task(
            id = "hour_income_yesno",
            theme = "Доходы и заработок",
            type = TaskType.YESNO,
            title = "✅ Быстрый вопрос",
            desc = "Карманные деньги — это твой доход?",
            reward = 15,
            choices = listOf(
                TaskChoice("Да", true, "Доход можно планировать."),
                TaskChoice("Нет", false, "Это тоже доход.")
            )
        ),

        /* ── Тема 5: Безопасность и разумность ────── */
        Task(
            id = "hour_safety",
            theme = "Безопасность и разумность",
            title = "🔒 Безопасность",
            desc = "Незнакомец просит данные карты за подарок. Что делать?",
            reward = 20,
            choices = listOf(
                TaskChoice("Не сообщать, рассказать взрослому", true, "Данные карты — секрет."),
                TaskChoice("Сообщить данные", false, "Это могут быть мошенники."),
                TaskChoice("Отправить фото карты", false, "Никогда так не делай.")
            )
        ),
        Task(
            id = "hour_ad",
            theme = "Безопасность и разумность",
            title = "📢 Реклама",
            desc = "В игре всплыла реклама: «Купи сейчас, только 1 минута!» Что делать?",
            reward = 20,
            choices = listOf(
                TaskChoice("Не спешить, подумать", true, "Спешка — друг мошенников."),
                TaskChoice("Скорее купить", false, "Реклама часто торопит."),
                TaskChoice("Нажать на всё", false, "Может быть опасно.")
            )
        )
    )

    /* ============================================================
       🌞 ЕЖЕДНЕВНЫЕ ЗАДАНИЯ — 9 штук
    ============================================================ */
    val dailyTasks = listOf(

        Task(
            id = "task_priority",
            theme = "Планирование бюджета",
            title = "🍎 Что купить первым?",
            desc = "Питомец голодный! Хватает только на одну покупку.",
            reward = 15,
            choices = listOf(
                TaskChoice("🍎 Яблоко — 5", true, "Верно! Сначала — самое важное."),
                TaskChoice("🎮 Мячик — 10", false, "Питомец рад мячику, но голодный."),
                TaskChoice("🧼 Мыло — 8", false, "Гигиена важна, но голод сильнее.")
            )
        ),
        Task(
            id = "task_income_expenses",
            theme = "Планирование бюджета",
            title = "💡 Расходы и доходы",
            desc = "Расходы больше доходов. Что это значит?",
            reward = 20,
            choices = listOf(
                TaskChoice("Денег не хватит, надо менять план", true, "Главное правило бюджета."),
                TaskChoice("Всё хорошо", false, "Денег не хватит."),
                TaskChoice("Нужно купить ещё", false, "Это ухудшит ситуацию.")
            )
        ),
        Task(
            id = "task_count_budget",
            theme = "Планирование бюджета",
            type = TaskType.COUNT,
            title = "🔢 Посчитай остаток",
            desc = "Доход 100. Ты отложил 30 в копилку и потратил 40 на обязательное. Сколько осталось?",
            reward = 40,
            answer = 30
        ),

        Task(
            id = "task_savings_goal",
            theme = "Сбережения и цели",
            title = "🎯 Мечта ближе",
            desc = "Питомец хочет домик за 200. Сейчас в «Целях» 80. Что делать?",
            reward = 20,
            choices = listOf(
                TaskChoice("Откладывать по 15 каждый день", true, "Через 8 дней — домик!"),
                TaskChoice("Забыть о мечте", false, "Никогда не сдавайся."),
                TaskChoice("Потратить 80 на игрушки", false, "Мечта ждёт.")
            )
        ),
        Task(
            id = "task_withdraw",
            theme = "Сбережения и цели",
            type = TaskType.YESNO,
            title = "✅ Быстрый вопрос",
            desc = "Можно ли снять деньги с копилки, если очень хочется?",
            reward = 15,
            choices = listOf(
                TaskChoice("Да, но цель отодвинется", true, "Снять можно, но подумай."),
                TaskChoice("Нет, никогда", false, "Можно, но лучше не спешить.")
            )
        ),
        Task(
            id = "task_count_goal",
            theme = "Сбережения и цели",
            type = TaskType.COUNT,
            title = "🔢 Сколько откладывать?",
            desc = "Цель 120 монет. Хочешь накопить за 6 дней. Сколько откладывать в день?",
            reward = 40,
            answer = 20
        ),

        Task(
            id = "task_compare",
            theme = "Покупки и платежи",
            title = "🏪 Сравни цены",
            desc = "Каша у Совы 10, у Ежа 8. Где выгоднее?",
            reward = 10,
            choices = listOf(
                TaskChoice("У Совы — 10", false, "Ближе, но дороже."),
                TaskChoice("У Ежа — 8", true, "Экономия 2 монеты!")
            )
        ),
        Task(
            id = "task_need_vs_want",
            theme = "Покупки и платежи",
            title = "🧠 Обязательное или желаемое?",
            desc = "Питомец голодный. Что купить в первую очередь?",
            reward = 20,
            choices = listOf(
                TaskChoice("Еду — это обязательное", true, "Сначала — еда."),
                TaskChoice("Бантик — красиво", false, "Голодный не рад бантику."),
                TaskChoice("Ничего, подожду", false, "Питомец загрустит.")
            )
        ),
        Task(
            id = "task_unexpected",
            theme = "Покупки и платежи",
            title = "🩺 Непредвиденный расход",
            desc = "Питомец простудился. Лекарство — 25. Что делать?",
            reward = 20,
            choices = listOf(
                TaskChoice("Купить лекарство", true, "Непредвиденное — часть бюджета."),
                TaskChoice("Игнорировать", false, "Заболеет сильнее."),
                TaskChoice("Купить игрушку вместо лекарства", false, "Лечение важнее.")
            )
        ),

        Task(
            id = "task_earn",
            theme = "Доходы и заработок",
            title = "💰 Как увеличить доход?",
            desc = "Что поможет получать больше монет?",
            reward = 20,
            choices = listOf(
                TaskChoice("Выполнять задания и помогать", true, "Заработок — результат усилий."),
                TaskChoice("Ждать, что появятся сами", false, "Не появятся."),
                TaskChoice("Просить у всех подряд", false, "Не поможет.")
            )
        )
    )

    /* ============================================================
       📅 ЕЖЕНЕДЕЛЬНЫЕ ЗАДАНИЯ — 3 штуки
    ============================================================ */
    val weeklyTasks = listOf(
        Task(
            id = "weekly_kind",
            theme = "Безопасность и разумность",
            title = "🔵 Помоги другу",
            desc = "У соседского питомца порвался мячик. Поможешь из «Подушки»?",
            reward = 50,
            choices = listOf(
                TaskChoice("Дать 15 из подушки", true, "Щедро! +50 🪙", reward = 50),
                TaskChoice("Дать 5", true, "Скромно, но приятно. +30 🪙", reward = 30),
                TaskChoice("Не давать", false, "Иногда помощь важнее монет.", reward = 0)
            )
        ),
        Task(
            id = "weekly_ads",
            theme = "Безопасность и разумность",
            title = "🔵 Скидка на ненужное",
            desc = "Ты увидел скидку 50% на то, что тебе не нужно. Что делать?",
            reward = 40,
            choices = listOf(
                TaskChoice("Подумать: нужно ли вообще", true, "Скидка — не повод покупать."),
                TaskChoice("Скорее купить", false, "Скидка на ненужное — не экономия."),
                TaskChoice("Купить два", false, "Ещё хуже.")
            )
        ),
        Task(
            id = "weekly_password",
            theme = "Безопасность и разумность",
            type = TaskType.YESNO,
            title = "🔵 Пароль — секрет?",
            desc = "Можно ли сообщать пароль от копилки незнакомым?",
            reward = 15,
            choices = listOf(
                TaskChoice("Нет", true, "Пароль — только твой секрет."),
                TaskChoice("Да", false, "Пароль нельзя сообщать никому, кроме родителей.")
            )
        )
    )

    /** Все задания одним списком — удобно для поиска по id. */
    val allTasks: List<Task> get() = hourlyTasks + dailyTasks + weeklyTasks
}
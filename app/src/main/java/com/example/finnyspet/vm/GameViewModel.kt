package com.example.finnyspet.vm

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnyspet.data.Content
import com.example.finnyspet.data.Dream
import com.example.finnyspet.data.GamePrefs
import com.example.finnyspet.data.GameState
import com.example.finnyspet.data.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.random.Random

class GameViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = GamePrefs(app)
    val state = mutableStateOf(prefs.load())

    // ── Сообщения Совы ────────────────────────────────────
    val sovaMessage = mutableStateOf<String?>(null)
    private val sovaQueue = ArrayDeque<Pair<String, Long>>()
    private var sovaJob: Job? = null

    fun showSova(text: String, durationMs: Long = 2200L, gapMs: Long = 300L) {
        sovaQueue.addLast(text to durationMs)
        if (sovaJob?.isActive == true) return
        sovaJob = viewModelScope.launch {
            while (sovaQueue.isNotEmpty()) {
                val (msg, dur) = sovaQueue.removeFirst()
                sovaMessage.value = msg
                delay(dur)
                sovaMessage.value = null
                if (sovaQueue.isNotEmpty()) delay(gapMs)
            }
        }
    }

    private var tickerJob: Job? = null

    init {
        startTicker()
    }

    /* ============================================================
       ⏱ ТИК РАЗ В МИНУТУ
    ============================================================ */
    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            delay(2000)
            checkDailyTick()

            while (true) {
                delay(60_000L)
                if (state.value.petId == null) continue
                tickStats()
            }
        }
    }

    private fun tickStats() {
        val s = state.value
        if (s.petId == null) return

        var food = (s.food - 1f).coerceIn(0f, 100f)
        var mood = s.mood
        var energy = s.energy
        var health = s.health

        if (food <= 90f) {
            mood = (mood - 0.2f).coerceIn(0f, 100f)
            energy = (energy - 0.2f).coerceIn(0f, 100f)
        }
        if (food <= 80f && mood <= 80f) {
            health = (health - 0.5f).coerceIn(0f, 100f)
        }

        var newState = s.copy(
            food = food, mood = mood, energy = energy, health = health,
            lastTick = System.currentTimeMillis()
        )

        val now = System.currentTimeMillis()
        if (newState.nextHourAt == 0L || now >= newState.nextHourAt) {
            newState = newState.copy(nextHourAt = now + 60 * 60 * 1000L)
            update(newState)
            refreshHourlyTasks()
        } else {
            update(newState)
        }

        when {
            food < 20f -> showSova("🚨 Питомец очень голодный! Срочно покорми 🍎")
            food < 40f -> showSova("⚠️ Питомец проголодался. Нужна еда 🍎")
            health < 30f -> showSova("⚠️ Питомцу нездоровится. Купи лекарство 💊")
            mood < 30f -> showSova("😔 Питомец загрустил. Поиграй с ним 🎮")
        }
    }

    /* ============================================================
       📋 РОТАЦИЯ ЗАДАНИЙ
    ============================================================ */
    private fun refreshHourlyTasks() {
        val s = state.value
        val done = s.doneTaskIds.split(",").filter { it.isNotBlank() }
        val current = s.activeHourlyIds.split(",").filter { it.isNotBlank() }
        val pool = Content.hourlyTasks.filter { it.id !in done && it.id !in current }
        val picked = if (pool.size >= 3) pool.shuffled().take(3)
        else Content.hourlyTasks.shuffled().take(3)
        update(s.copy(activeHourlyIds = picked.joinToString(",") { it.id }))
    }

    private fun refreshDailyTasks() {
        val s = state.value
        val done = s.doneTaskIds.split(",").filter { it.isNotBlank() }
        val current = s.activeDailyIds.split(",").filter { it.isNotBlank() }
        val pool = Content.dailyTasks.filter { it.id !in done && it.id !in current }
        val picked = if (pool.size >= 2) pool.shuffled().take(2)
        else Content.dailyTasks.shuffled().take(2)
        update(s.copy(activeDailyIds = picked.joinToString(",") { it.id }))
    }

    private fun refreshWeeklyTasks() {
        val s = state.value
        val done = s.doneTaskIds.split(",").filter { it.isNotBlank() }
        val current = s.activeWeeklyIds.split(",").filter { it.isNotBlank() }
        val pool = Content.weeklyTasks.filter { it.id !in done && it.id !in current }
        val picked = if (pool.isNotEmpty()) pool.shuffled().take(1)
        else Content.weeklyTasks.shuffled().take(1)
        update(s.copy(activeWeeklyIds = picked.joinToString(",") { it.id }))
    }

    fun getActiveHourlyTasks(): List<Task> {
        val s = state.value
        if (s.demoShowAllTasks) return Content.hourlyTasks
        val ids = s.activeHourlyIds.split(",").filter { it.isNotBlank() }
        return Content.hourlyTasks.filter { it.id in ids }
    }

    fun getActiveDailyTasks(): List<Task> {
        val s = state.value
        if (s.demoShowAllTasks) return Content.dailyTasks
        val ids = s.activeDailyIds.split(",").filter { it.isNotBlank() }
        return Content.dailyTasks.filter { it.id in ids }
    }

    fun getActiveWeeklyTasks(): List<Task> {
        val s = state.value
        if (s.demoShowAllTasks) return Content.weeklyTasks
        val ids = s.activeWeeklyIds.split(",").filter { it.isNotBlank() }
        return Content.weeklyTasks.filter { it.id in ids }
    }

    fun isTaskDone(taskId: String): Boolean =
        taskId in state.value.doneTaskIds.split(",").filter { it.isNotBlank() }

    /* ============================================================
       🌅 ЕЖЕДНЕВНЫЙ ЦИКЛ
    ============================================================ */
    private fun checkDailyTick() {
        val s = state.value
        if (s.petId == null) return

        val today = today()
        if (s.lastLogin == today) {
            if (s.activeDailyIds.isBlank()) refreshDailyTasks()
            if (s.activeWeeklyIds.isBlank()) refreshWeeklyTasks()
            if (s.activeHourlyIds.isBlank()) refreshHourlyTasks()
            return
        }

        val newStreak = if (s.lastLogin.isEmpty()) 1 else {
            val diff = LocalDate.parse(today).toEpochDay() - LocalDate.parse(s.lastLogin).toEpochDay()
            if (diff == 1L) s.streak + 1 else 1
        }

        val streakBonus = when {
            newStreak >= 30 -> 20
            newStreak >= 14 -> 15
            newStreak >= 7  -> 10
            newStreak >= 3  -> 5
            else            -> 0
        }
        val income = Content.dailyIncome + streakBonus

        var newState = s.copy(
            coins = s.coins + income,
            unallocated = s.unallocated + income,
            weekEarned = s.weekEarned + income,
            streak = newStreak,
            lastLogin = today
        )

        var interest = 0
        if (newStreak % 7 == 0 && newState.walletGoal > 0) {
            val pet = Content.pets.find { it.id == newState.petId }
            var i = (newState.walletGoal * 0.05f).toInt()
            if (pet?.bonusKey == "interestBonus") i = (i * 1.05f).toInt()
            interest = i.coerceAtMost(100)
            newState = newState.copy(walletGoal = newState.walletGoal + interest)
        }

        val pet = Content.pets.find { it.id == newState.petId }
        if (pet?.bonusKey == "dailyMood") {
            newState = newState.copy(mood = (newState.mood + 5f).coerceAtMost(100f))
        }

        update(newState)
        refreshDailyTasks()
        refreshWeeklyTasks()
        if (state.value.activeHourlyIds.isBlank()) refreshHourlyTasks()

        showSova("🌅 Новый день! +$income 🪙")
        if (streakBonus > 0) showSova("🔥 Серия $newStreak дней! Бонус +$streakBonus 🪙")
        if (interest > 0) showSova("💰 Проценты на копилку: +$interest 🪙")
        if (pet?.bonusKey == "dailyMood") showSova("🐼 Панда-облачко: +5 к настроению 💖")

        viewModelScope.launch {
            delay(3400)
            triggerRandomEvent()
        }
    }

    private fun triggerRandomEvent() {
        if (Random.nextFloat() >= 0.20f) return
        val s = state.value
        if (s.petId == null) return

        val pet = Content.pets.find { it.id == s.petId }
        val shielded = pet?.bonusKey == "eventShield"

        data class Event(val name: String, val cost: Int, val emoji: String)
        val pool = listOf(
            Event("Питомец простудился", 20, "🤒"),
            Event("Порвался мячик", 10, "🎾"),
            Event("Питомец загрустил", 15, "😔"),
            Event("Сломалась игрушка", 12, "🧸"),
            Event("Питомец промочил лапки", 8, "💧")
        )
        val event = pool.random()
        val cost = if (shielded) (event.cost * 0.8f).toInt() else event.cost

        when {
            s.walletCushion >= cost -> {
                update(s.copy(walletCushion = s.walletCushion - cost))
                showSova("${event.emoji} ${event.name}! −$cost 🪙 из «Подушки»")
            }
            s.walletGoal >= cost -> {
                update(s.copy(walletGoal = s.walletGoal - cost))
                showSova("${event.emoji} ${event.name}! −$cost 🪙 из «Целей»")
            }
            else -> {
                update(s.copy(mood = (s.mood - 15f).coerceIn(0f, 100f)))
                showSova("${event.emoji} ${event.name}! Денег нет — питомец расстроился 😔")
            }
        }
    }

    /* ============================================================
       ОСНОВНЫЕ ДЕЙСТВИЯ
    ============================================================ */
    fun startGame(petId: String) {
        val pet = Content.pets.find { it.id == petId } ?: return
        val bonus = if (pet.bonusKey == "startMoney") 100 else 50
        val newState = GameState(
            petId = pet.id,
            petName = pet.name,
            coins = bonus + Content.dailyIncome,
            unallocated = bonus + Content.dailyIncome,
            streak = 1,
            lastLogin = today()
        )
        update(newState)
        refreshHourlyTasks()
        refreshDailyTasks()
        refreshWeeklyTasks()
        showSova("🐾 Добро пожаловать! Начнём с ${bonus + Content.dailyIncome} 🪙")
    }

    fun update(newState: GameState) {
        state.value = newState
        prefs.save(newState)
    }

    fun petTap() {
        val s = state.value
        val phrases = listOf("💖 Питомец рад!", "😊 Хи-хи!", "🤗 Обнимашки!", "🎉 Ура!", "💫 Мур!")
        update(s.copy(mood = (s.mood + 1f).coerceAtMost(100f)))
        showSova(phrases.random())
    }

    fun renamePet(newName: String) {
        update(state.value.copy(petName = newName.take(12)))
        showSova("✏️ Имя сохранено!")
    }

    fun buyItem(itemId: String, tab: String) {
        val s = state.value
        val item = when (tab) {
            "food" -> Content.shopFood.find { it.id == itemId }
            "play" -> Content.shopPlay.find { it.id == itemId }
            else   -> Content.shopCare.find { it.id == itemId }
        } ?: return

        if (s.walletSpend < item.price) {
            showSova("⚠️ Мало монет в «Тратах» 🟢")
            return
        }

        update(
            s.copy(
                walletSpend = s.walletSpend - item.price,
                weekSpent = s.weekSpent + item.price,
                food = (s.food + item.food).coerceIn(0f, 100f),
                mood = (s.mood + item.mood).coerceIn(0f, 100f),
                energy = (s.energy + item.energy).coerceIn(0f, 100f),
                health = (s.health + item.health).coerceIn(0f, 100f),
                progress = s.progress + 2
            )
        )
        showSova("${item.emoji} ${item.name}! ${item.desc}")
    }

    fun completeTask(taskId: String, reward: Int) {
        val s = state.value
        val done = s.doneTaskIds.split(",").filter { it.isNotBlank() }.toMutableList()
        if (taskId in done) return
        done.add(taskId)

        update(
            s.copy(
                coins = s.coins + reward,
                unallocated = s.unallocated + reward,
                weekEarned = s.weekEarned + reward,
                weekTasks = s.weekTasks + 1,
                progress = s.progress + 10,
                doneTaskIds = done.joinToString(",")
            )
        )
        showSova("✅ Задание выполнено! +$reward 🪙")
    }

    fun distribute(spend: Int, goal: Int, cushion: Int) {
        val s = state.value
        val total = s.unallocated

        if (total <= 0) {
            showSova("Пока нечего распределять 🏦")
            return
        }
        if (spend < 0 || goal < 0 || cushion < 0) return
        if (spend + goal + cushion != total) {
            showSova("Нужно разложить все монетки!")
            return
        }

        val smart = spend.toFloat() / total in 0.5f..0.7f &&
                goal.toFloat() / total in 0.2f..0.4f &&
                cushion.toFloat() / total in 0.05f..0.15f
        val bonus = if (smart) 10 else 0

        update(
            s.copy(
                walletSpend   = s.walletSpend + spend + bonus,
                walletGoal    = s.walletGoal + goal,
                walletCushion = s.walletCushion + cushion,
                unallocated   = 0,
                coins         = s.coins + bonus,
                weekSaved     = s.weekSaved + goal + cushion,
                mood          = (s.mood + if (smart) 5f else 0f).coerceAtMost(100f)
            )
        )
        if (smart) showSova("🎉 Умный бюджет! +10 🪙 бонус")
        else showSova("🏦 Монетки разложены!")
    }

    fun withdrawFromCushion(amount: Int) {
        val s = state.value
        if (amount <= 0) return
        if (s.walletCushion < amount) {
            showSova("⚠️ В «Подушке» только ${s.walletCushion} 🪙")
            return
        }
        update(
            s.copy(
                walletCushion = s.walletCushion - amount,
                walletSpend = s.walletSpend + amount
            )
        )
        showSova("💸 Взято из подушки: $amount 🪙")
    }

    /* ============================================================
       🎯 МЕЧТЫ
    ============================================================ */
    fun getCurrentDream(): Dream? {
        val id = state.value.currentDreamId
        if (id.isBlank()) return null
        return Content.dreams.find { it.id == id }
    }

    fun selectDream(dreamId: String) {
        val dream = Content.dreams.find { it.id == dreamId } ?: return
        val s = state.value
        if (s.dreamsOwned.split(",").contains(dreamId)) {
            showSova("Эта мечта уже сбылась 💖")
            return
        }
        update(s.copy(currentDreamId = dreamId))
        showSova("🎯 Цель: ${dream.emoji} ${dream.name}")
    }

    fun addToGoal(amount: Int) {
        val s = state.value
        if (amount <= 0) return
        if (s.walletSpend < amount) {
            showSova("⚠️ В «Тратах» только ${s.walletSpend} 🪙")
            return
        }
        update(
            s.copy(
                walletSpend = s.walletSpend - amount,
                walletGoal = s.walletGoal + amount
            )
        )
        showSova("🏦 В копилку: +$amount 🪙")
    }

    fun buyDream(dreamId: String) {
        val dream = Content.dreams.find { it.id == dreamId } ?: return
        val s = state.value

        if (s.dreamsOwned.split(",").contains(dreamId)) {
            showSova("Эта мечта уже сбылась 💖")
            return
        }
        if (s.walletGoal < dream.price) {
            showSova("⚠️ Не хватает ${dream.price - s.walletGoal} 🪙")
            return
        }

        val owned = s.dreamsOwned.split(",").filter { it.isNotBlank() }.toMutableList()
        owned.add(dreamId)

        update(
            s.copy(
                walletGoal = s.walletGoal - dream.price,
                dreamsOwned = owned.joinToString(","),
                mood = (s.mood + 25f).coerceAtMost(100f),
                progress = s.progress + 20,
                currentDreamId = ""
            )
        )
        showSova("🎉 Мечта сбылась: ${dream.emoji} ${dream.name}!")
    }

    fun isDreamOwned(dreamId: String): Boolean =
        state.value.dreamsOwned.split(",").contains(dreamId)

    /* ============================================================
       🎬 ДЕМО-РЕЖИМ
    ============================================================ */
    fun demoTickDay() {
        val s = state.value
        if (s.petId == null) {
            showSova("Сначала выбери питомца")
            return
        }
        val income = Content.dailyIncome + 5
        update(
            s.copy(
                coins = s.coins + income,
                unallocated = s.unallocated + income,
                weekEarned = s.weekEarned + income,
                streak = s.streak + 1,
                lastLogin = today()
            )
        )
        refreshDailyTasks()
        refreshWeeklyTasks()
        refreshHourlyTasks()
        showSova("⏩ День прокручен! +$income 🪙")
    }

    fun demoTickHour() {
        val s = state.value
        if (s.petId == null) {
            showSova("Сначала выбери питомца")
            return
        }
        update(s.copy(nextHourAt = 0L))
        refreshHourlyTasks()
        showSova("⏰ Час прокручен! Новые почасовые задания")
    }

    fun demoShowAllTasks() {
        val s = state.value
        if (s.petId == null) {
            showSova("Сначала выбери питомца")
            return
        }
        val allHourly = Content.hourlyTasks.joinToString(",") { it.id }
        val allDaily = Content.dailyTasks.joinToString(",") { it.id }
        val allWeekly = Content.weeklyTasks.joinToString(",") { it.id }
        update(
            s.copy(
                demoShowAllTasks = true,
                activeHourlyIds = allHourly,
                activeDailyIds = allDaily,
                activeWeeklyIds = allWeekly
            )
        )
        showSova("📋 Все задания открыты")
    }

    fun demoReset() {
        prefs.clearAll()
        state.value = GameState()
    }

    /* ============================================================
       ПАРОЛЬ ВЗРОСЛОГО
    ============================================================ */
    fun adultHash(): String? = prefs.getAdultHash()
    fun saveAdultPassword(plain: String) {
        prefs.setAdultHash(prefs.hashPassword(plain))
        showSova("🔐 Пароль установлен")
    }
    fun clearAdultPassword() = prefs.setAdultHash(null)
    fun checkPassword(plain: String): Boolean =
        prefs.getAdultHash() == prefs.hashPassword(plain)

    fun isOnboardingDone(): Boolean = prefs.isOnboardingDone()
    fun setOnboardingDone() = prefs.setOnboardingDone()
    fun resetOnboarding() = prefs.resetOnboarding()

    fun resetAll() {
        prefs.clearAll()
        state.value = GameState()
    }

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
        sovaJob?.cancel()
        sovaQueue.clear()
    }

    private fun today(): String = LocalDate.now().toString()
}
package com.example.finnyspet.data

import android.content.Context
import androidx.core.content.edit

class GamePrefs(context: Context) {

    private val sp = context.getSharedPreferences("finnyspet_prefs", Context.MODE_PRIVATE)

    fun load(): GameState {
        return GameState(
            petId       = sp.getString("petId", null),
            petName     = sp.getString("petName", "Питомец") ?: "Питомец",
            coins       = sp.getInt("coins", 50),
            walletSpend = sp.getInt("walletSpend", 0),
            walletGoal  = sp.getInt("walletGoal", 0),
            walletCushion = sp.getInt("walletCushion", 0),
            food        = sp.getFloat("food", 100f),
            mood        = sp.getFloat("mood", 100f),
            energy      = sp.getFloat("energy", 100f),
            health      = sp.getFloat("health", 100f),
            progress    = sp.getInt("progress", 0),
            streak      = sp.getInt("streak", 0),
            weekEarned  = sp.getInt("weekEarned", 0),
            weekSpent   = sp.getInt("weekSpent", 0),
            weekSaved   = sp.getInt("weekSaved", 0),
            weekTasks   = sp.getInt("weekTasks", 0),
            unallocated = sp.getInt("unallocated", 0),
            lastTick    = sp.getLong("lastTick", System.currentTimeMillis()),
            lastLogin   = sp.getString("lastLogin", "") ?: "",
            foodTriggered90 = sp.getBoolean("foodTriggered90", false),
            dreamsOwned = sp.getString("dreamsOwned", "") ?: "",
            currentDreamId = sp.getString("currentDreamId", "") ?: "",
            activeHourlyIds = sp.getString("activeHourlyIds", "") ?: "",
            activeDailyIds  = sp.getString("activeDailyIds", "") ?: "",
            activeWeeklyIds = sp.getString("activeWeeklyIds", "") ?: "",
            doneTaskIds     = sp.getString("doneTaskIds", "") ?: "",
            nextHourAt      = sp.getLong("nextHourAt", 0L),
            demoShowAllTasks = sp.getBoolean("demoShowAllTasks", false)
        )
    }

    fun save(state: GameState) {
        sp.edit {
            putString("petId", state.petId)
            putString("petName", state.petName)
            putInt("coins", state.coins)
            putInt("walletSpend", state.walletSpend)
            putInt("walletGoal", state.walletGoal)
            putInt("walletCushion", state.walletCushion)
            putFloat("food", state.food)
            putFloat("mood", state.mood)
            putFloat("energy", state.energy)
            putFloat("health", state.health)
            putInt("progress", state.progress)
            putInt("streak", state.streak)
            putInt("weekEarned", state.weekEarned)
            putInt("weekSpent", state.weekSpent)
            putInt("weekSaved", state.weekSaved)
            putInt("weekTasks", state.weekTasks)
            putInt("unallocated", state.unallocated)
            putLong("lastTick", state.lastTick)
            putString("lastLogin", state.lastLogin)
            putBoolean("foodTriggered90", state.foodTriggered90)
            putString("dreamsOwned", state.dreamsOwned)
            putString("currentDreamId", state.currentDreamId)
            putString("activeHourlyIds", state.activeHourlyIds)
            putString("activeDailyIds", state.activeDailyIds)
            putString("activeWeeklyIds", state.activeWeeklyIds)
            putString("doneTaskIds", state.doneTaskIds)
            putLong("nextHourAt", state.nextHourAt)
            putBoolean("demoShowAllTasks", state.demoShowAllTasks)
        }
    }

    fun getAdultHash(): String? = sp.getString("adultHash", null)
    fun setAdultHash(hash: String?) = sp.edit {
        if (hash == null) remove("adultHash") else putString("adultHash", hash)
    }

    fun hashPassword(str: String): String {
        var h = 5381
        for (c in str) h = (h shl 5) + h + c.code
        return "h" + Integer.toUnsignedString(h, 36)
    }

    fun isOnboardingDone(): Boolean = sp.getBoolean("onboarding_done", false)
    fun setOnboardingDone() = sp.edit { putBoolean("onboarding_done", true) }
    fun resetOnboarding() = sp.edit { remove("onboarding_done") }

    fun clearAll() {
        sp.edit { clear() }
    }
}
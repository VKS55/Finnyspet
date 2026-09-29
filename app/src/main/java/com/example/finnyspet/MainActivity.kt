package com.example.finnyspet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finnyspet.ui.screens.AdultScreen
import com.example.finnyspet.ui.screens.ChoosePetScreen
import com.example.finnyspet.ui.screens.DemoScreen
import com.example.finnyspet.ui.screens.DreamsScreen
import com.example.finnyspet.ui.screens.MainScreen
import com.example.finnyspet.ui.screens.OnboardingScreen
import com.example.finnyspet.ui.screens.ShopScreen
import com.example.finnyspet.ui.screens.TasksScreen
import com.example.finnyspet.ui.screens.WalletScreen
import com.example.finnyspet.ui.theme.FinnyspetTheme
import com.example.finnyspet.vm.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinnyspetTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val vm: GameViewModel = viewModel()
    val state by vm.state
    var screen by remember { mutableStateOf("") }

    var onboardingDone by remember { mutableStateOf(vm.isOnboardingDone()) }
    LaunchedEffect(state.petId) {
        onboardingDone = vm.isOnboardingDone()
    }

    if (!onboardingDone) {
        OnboardingScreen(onFinish = {
            vm.setOnboardingDone()
            onboardingDone = true
        })
        return
    }

    if (state.petId == null) {
        ChoosePetScreen(vm, onStart = { screen = "main" })
        return
    }

    when {
        screen == "main" || screen == "" -> {
            MainScreen(
                vm = vm,
                onShop = { tab -> screen = "shop:$tab" },
                onTasks = { screen = "tasks" },
                onDreams = { screen = "dreams" },
                onWallet = { screen = "wallet" },
                onAdult = { screen = "adult" }
            )
        }

        screen == "tasks" -> TasksScreen(vm, onBack = { screen = "main" })
        screen == "dreams" -> DreamsScreen(vm, onBack = { screen = "main" })
        screen == "wallet" -> WalletScreen(vm, onBack = { screen = "main" })

        screen == "adult" -> AdultScreen(
            vm = vm,
            onBack = { screen = "main" },
            onOpenDemo = { screen = "demo" }
        )

        screen == "demo" -> DemoScreen(
            vm = vm,
            onBack = { screen = "adult" }
        )

        screen.startsWith("shop:") -> {
            val tab = screen.removePrefix("shop:")
            ShopScreen(vm, tab = tab, onBack = { screen = "main" })
        }

        else -> {
            screen = "main"
            MainScreen(
                vm = vm,
                onShop = { tab -> screen = "shop:$tab" },
                onTasks = { screen = "tasks" },
                onDreams = { screen = "dreams" },
                onWallet = { screen = "wallet" },
                onAdult = { screen = "adult" }
            )
        }
    }
}
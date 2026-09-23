package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.AuraDeepNavy
import com.example.ui.theme.AuraverseTheme
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuraverseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AuraDeepNavy
                ) {
                    val viewModel: AuraverseViewModel = viewModel()
                    AuraverseApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun AuraverseApp(viewModel: AuraverseViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Handle Back Press navigation
    BackHandler(enabled = currentScreen != ScreenRoute.MAIN_MENU) {
        viewModel.navigateTo(ScreenRoute.MAIN_MENU)
    }

    when (currentScreen) {
        ScreenRoute.MAIN_MENU -> MainMenuScreen(viewModel)
        ScreenRoute.WORLD -> WorldExplorerScreen(viewModel)
        ScreenRoute.AVATAR_CREATOR -> AvatarCreatorScreen(viewModel)
        ScreenRoute.MINI_GAMES -> MiniGamesScreen(viewModel)
        ScreenRoute.CREATOR_STUDIO -> CreatorStudioScreen(viewModel)
        ScreenRoute.MARKETPLACE -> MarketplaceScreen(viewModel)
        ScreenRoute.INVENTORY -> InventoryScreen(viewModel)
        ScreenRoute.SOCIAL -> SocialScreen(viewModel)
        ScreenRoute.SETTINGS -> SettingsScreen(viewModel)
    }
}

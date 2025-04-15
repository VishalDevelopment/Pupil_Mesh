package com.example.pupilmesh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pupilmesh.Common.Routes.Routes
import com.example.pupilmesh.Domain_Layer.Modal.Data
import com.example.pupilmesh.Ui_Layer.Screens.HomeScreen
import com.example.pupilmesh.Ui_Layer.Screens.LoginScreen
import com.example.pupilmesh.Ui_Layer.Screens.MangaDetailScreen
import com.example.pupilmesh.Ui_Layer.Screens.SignupScreen
import com.example.pupilmesh.Ui_Layer.Viewmodel.PupilVm
import com.example.pupilmesh.ui.theme.PupilMeshTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PupilMeshTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier=Modifier.padding(innerPadding)){
                        StartApp()
                    }
                }
            }
        }
    }

    @Composable
    private fun StartApp() {
        val navController = rememberNavController()
        val viewmodel: PupilVm = hiltViewModel()
        val startDestination by viewmodel.startDestination.collectAsState()
        var specificmanga = Data(
            authors = emptyList(),
            create_at = 0,
            genres = emptyList(),
            id="",
            nsfw = false,
            status = "",
            sub_title = "",
            summary = "",
            thumb = "",
            title = "",
            total_chapter = 0,
            type = "",
            update_at = 0
        )
        if (startDestination.isNotBlank()) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable(Routes.LoginRoute.routes) {
                    LoginScreen({
                            navController.navigate(Routes.SignUpRoute.routes)
                        },
                        {
                            navController.navigate(Routes.HomeRoute.routes) {
                                popUpTo(Routes.LoginRoute.routes) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.SignUpRoute.routes) {
                    SignupScreen {
                        navController.navigateUp()
                    }
                }

                composable(Routes.HomeRoute.routes) {
                    HomeScreen(viewmodel) { content ->
                        specificmanga = content
                        navController.navigate(Routes.MangaDetailRoute.routes)
                    }
                }
                composable(Routes.MangaDetailRoute.routes) {

                    MangaDetailScreen( specificmanga)
                }
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}



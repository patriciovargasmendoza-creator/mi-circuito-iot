package cl.inacap.myapplication

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.inacap.myapplication.data.repository.AppRepository
import cl.inacap.myapplication.ui.screens.ActionListScreen
import cl.inacap.myapplication.ui.screens.CircuitControlScreen
import cl.inacap.myapplication.ui.screens.LoginScreen
import cl.inacap.myapplication.ui.screens.MenuScreen
import cl.inacap.myapplication.ui.screens.ProfileScreen
import cl.inacap.myapplication.ui.screens.RegisterScreen
import cl.inacap.myapplication.ui.screens.StatisticsScreen
import cl.inacap.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Impide capturas de pantalla con datos personales y del circuito
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IotApp()
                }
            }
        }
    }
}

@Composable
fun IotApp() {
    val context = LocalContext.current
    val repository = remember { AppRepository.getInstance(context) }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                repository = repository,
                onLoginSuccess = {
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                repository = repository,
                onRegisterSuccess = {
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("menu") {
            MenuScreen(
                repository = repository,
                onNavigateToCircuit = {
                    navController.navigate("circuit")
                },
                onNavigateToActions = {
                    navController.navigate("actions")
                },
                onNavigateToStats = {
                    navController.navigate("stats")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("circuit") {
            CircuitControlScreen(
                repository = repository,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("actions") {
            ActionListScreen(
                repository = repository,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("stats") {
            StatisticsScreen(
                repository = repository,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                repository = repository,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

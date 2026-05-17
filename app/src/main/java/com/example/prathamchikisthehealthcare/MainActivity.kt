package com.example.prathamchikisthehealthcare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.prathamchikisthehealthcare.ui.theme.PrathamChikistheHealthcareTheme
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : ComponentActivity() {
    lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale("kn", "IN")
            }
        }

        setContent {
            PrathamChikistheHealthcareTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {

                    composable("splash") {
                        SplashScreen(navController)
                    }

                    composable("login") {
                        LoginScreen(navController)
                    }

                    composable("home") {
                        HomeScreen(navController)
                    }

                    composable("snake") {
                        SnakeBiteScreen(navController, tts)
                    }

                    composable("heart") {
                        HeartAttackScreen(navController)
                    }

                    composable("burns") {
                        BurnsScreen(navController)
                    }

                    composable("fracture") {
                        FractureScreen(navController)
                    }

                    composable("hospital") {
                        HospitalScreen(navController)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000)
        )
        delay(1500)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Pratham Chikitse",
            modifier = Modifier.scale(scale.value),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun AnimatedButton(text: String, loading: Boolean = false, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        label = "buttonScale"
    )

    Button(
        onClick = {
            if (!loading) {
                pressed = true
                onClick()
                pressed = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale),
        enabled = !loading
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "loginAlpha"
    )

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
                .alpha(alpha),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pratham Chikitse Login",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedButton(
                text = "Login",
                loading = loading,
                onClick = {
                    if (username.trim() == "admin" && password.trim() == "1234") {
                        loading = true
                        navController.navigate("home")
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Emergency Help",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmergencyCard("🐍 Snake Bite") {
                navController.navigate("snake")
            }

            EmergencyCard("❤️ Heart Attack") {
                navController.navigate("heart")
            }

            EmergencyCard("🔥 Burns") {
                navController.navigate("burns")
            }

            EmergencyCard("🦴 Fracture") {
                navController.navigate("fracture")
            }

            EmergencyCard("🏥 Nearby Hospitals") {
                navController.navigate("hospital")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Emergency Contacts",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            EmergencyCard("🚑 Ambulance (108)") {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:108")
                navController.context.startActivity(intent)
            }

            EmergencyCard("👮 Police (100)") {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:100")
                navController.context.startActivity(intent)
            }

            EmergencyCard("🔥 Fire (101)") {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:101")
                navController.context.startActivity(intent)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HospitalScreen(navController: NavController) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            AnimatedButton(text = "⬅ Back", onClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Nearby Hospitals",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("1. Government Hospital - 108 Service Available")
            Text("2. Apollo Hospital")
            Text("3. Manipal Hospital")
            Text("4. District Hospital")
        }
    }
}

@Composable
fun EmergencyCard(title: String, onClick: () -> Unit) {
    var animate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animate = true
    }

    val offsetY by animateDpAsState(
        targetValue = if (animate) 0.dp else 50.dp,
        animationSpec = tween(500),
        label = "offsetY"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .offset(y = offsetY)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun SnakeBiteScreen(navController: NavController, tts: TextToSpeech) {
    val textToSpeak = """
        ಶಾಂತವಾಗಿರಿ.
        ರೋಗಿಯನ್ನು ಚಲಿಸಬೇಡಿ.
        ವಿಷವನ್ನು ಹೀರಬೇಡಿ.
        ತಕ್ಷಣ ಆಸ್ಪತ್ರೆಗೆ ಹೋಗಿ.
    """.trimIndent()

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            AnimatedButton(text = "⬅ Back", onClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Snake Bite First Aid",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("1. Stay calm (ಶಾಂತವಾಗಿರಿ)")
            Text("2. Keep patient still (ರೋಗಿಯನ್ನು ಚಲಿಸಬೇಡಿ)")
            Text("3. Do NOT suck venom (ವಿಷವನ್ನು ಹೀರಬೇಡಿ)")
            Text("4. Go to hospital immediately (ತಕ್ಷಣ ಆಸ್ಪತ್ರೆಗೆ ಹೋಗಿ)")

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedButton(
                text = "🔊 Play Audio (Kannada)",
                onClick = {
                    tts.language = Locale("kn", "IN")
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            )
        }
    }
}

@Composable
fun HeartAttackScreen(navController: NavController) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            AnimatedButton(text = "⬅ Back", onClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Heart Attack First Aid",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("1. Call emergency immediately")
            Text("2. Make person sit and relax")
            Text("3. Give aspirin (if available)")
            Text("4. Start CPR if unconscious")
        }
    }
}

@Composable
fun BurnsScreen(navController: NavController) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            AnimatedButton(text = "⬅ Back", onClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Burns First Aid",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("1. Cool burn with water")
            Text("2. Do NOT apply ice")
            Text("3. Cover with clean cloth")
            Text("4. Seek medical help")
        }
    }
}

@Composable
fun FractureScreen(navController: NavController) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            AnimatedButton(text = "⬅ Back", onClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Fracture First Aid",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("1. Keep area still")
            Text("2. Use splint if possible")
            Text("3. Apply ice pack")
            Text("4. Go to hospital")
        }
    }
}

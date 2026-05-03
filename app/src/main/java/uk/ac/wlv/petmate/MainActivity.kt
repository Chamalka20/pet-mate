package uk.ac.wlv.petmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import uk.ac.wlv.petmate.components.CustomSnackbarHost
import uk.ac.wlv.petmate.navigation.NavGraph
import uk.ac.wlv.petmate.ui.theme.PetMateTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Android 12+ Splash Screen
        installSplashScreen()

        super.onCreate(savedInstanceState)


        setContent {
            PetMateTheme(dynamicColor = false ) {

                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                    CustomSnackbarHost()
                }

            }
        }
    }
}


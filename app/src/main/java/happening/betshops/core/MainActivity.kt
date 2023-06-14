package happening.betshops.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import happening.betshops.core.theme.BetshopsTheme
import happening.betshops.home.ui.HomeScreen

// TODO
// font materials
// provjeri jel sve ide u resurse
// preview function
// sp u dp
// comentiraj resurse?
// back button neka gasi aplikaciju
// gledaj edge caseve za sve
// gdje se nalazis inicijalizacija
// predugacak tekst, ogrnaici na 3 retka
// dobro pregledaj homescreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BetshopsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

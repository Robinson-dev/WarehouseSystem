package cl.arriagada.layoutjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.arriagada.layoutjc.ui.theme.ConstraintView
import cl.arriagada.layoutjc.ui.theme.LayoutJCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayoutJCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    LayoutView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun LayoutView(modifier: Modifier){
    ConstraintView(modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LayoutJCTheme {
        LayoutView(Modifier.padding(top = 24.dp))
        //Greeting("Android")
    }
}
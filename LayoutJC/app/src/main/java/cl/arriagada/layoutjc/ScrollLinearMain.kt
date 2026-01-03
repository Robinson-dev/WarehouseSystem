package cl.arriagada.layoutjc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.arriagada.layoutjc.ui.theme.LayoutJCTheme

@Preview
@Composable
fun ScrolllinearPreview(){
    LayoutJCTheme {
        ScrolllinearView(Modifier.padding(top = 24.dp))
    }
}

@Composable
fun ScrolllinearView(modifier: Modifier){
    Column (modifier = modifier
        .verticalScroll(rememberScrollState())
        .background(Color.Yellow)
        .fillMaxWidth()
        .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "wrap content",
            modifier = Modifier
                .background(Color.Cyan)
                .padding(horizontal = 16.dp))

        Text("match parent",
            modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .background(Color.Gray)
               )
        Button(onClick = {}){
            Text("Boton")
        }
        Button(onClick = {}){
            Text("Boton")
        }
        Button(onClick = {}){
            Text("Boton")
        }
        Button(onClick = {}){
            Text("Boton")
        }
        Button(onClick = {}){
            Text("Boton")
        }
        Button(onClick = {}){
            Text("Boton")
        }
        Button(onClick = {}){
            Text("Boton")
        }
        Row (modifier
            .fillMaxWidth()
            .background(Color.LightGray),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            Column (Modifier
                .size(width = 40.dp, height = 24.dp)
                .padding(end = 16.dp)
                .background(Color.Green)){  }
            Button(onClick = {}){
            Text("Boton")
            }
            Button(onClick = {},
                Modifier.padding(16.dp)){
                Text("Boton")
            }
            Button(onClick = {}){
                Text("Boton")
            }

        }
    }
}

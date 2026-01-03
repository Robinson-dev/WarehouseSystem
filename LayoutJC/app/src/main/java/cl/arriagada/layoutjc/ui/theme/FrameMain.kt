package cl.arriagada.layoutjc.ui.theme

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.arriagada.layoutjc.R
import cl.arriagada.layoutjc.ScrolllinearView


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FremeView() {
    LayoutJCTheme {
        FremeView(Modifier.padding(top = 24.dp))
    }
}

@Composable
fun FremeView(modifier: Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Red)
    ) {
        Image(
            painter = painterResource(R.drawable.kotlin_01),
            contentDescription = "imagen de kotlin",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.Magenta)
        )
        Text(
            "texto demostrativo",
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        )

        Box( modifier = Modifier.
            fillMaxSize(),
            contentAlignment = Alignment.Center) {
            TextField(
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Cyan,
                    focusedContainerColor = Color.White
                ),
                label = {
                    Text(text = "Email")
                        })
        }

            Box(Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color.Transparent),
                contentAlignment = Alignment.BottomEnd){
                Image(imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Boton de play",
                    modifier = Modifier
                        .padding(end=16.dp, bottom = 16.dp)
                        .background(Color.LightGray))
                Image(imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Boton de play2",
                    modifier = Modifier
                        .padding(end=56.dp, bottom = 16.dp)
                        .background(Color.LightGray))
            }
    }

}
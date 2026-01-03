package cl.arriagada.layoutjc.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cl.arriagada.layoutjc.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScrollLinearPreview() {
    LayoutJCTheme {
        ConstraintView(Modifier.padding(top = 24.dp))
    }
}

@Composable
fun ConstraintView(modifier: Modifier) {
    ConstraintLayout(
        modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        val (cvAd, imgCard, tvTitle,
            imgUrl, btnOne, btnTwo, btnThree, imgPlay, btnPlay, btnPlay2, cGuides) = createRefs()
        Text(
            "Constraint",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(cvAd) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .width(200.dp)
                .background(Color.Blue),
            textAlign = TextAlign.Center)

        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(imgCard) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .background(Color.DarkGray))
        Text(
            "varios Margenes...",
            Modifier
                .padding(8.dp)
                .constrainAs(tvTitle) {
                    start.linkTo(imgCard.end)
                    end.linkTo(parent.end)
                    top.linkTo(imgCard.top)
                    width = Dimension.fillToConstraints
                }
                .background(Color.LightGray),
            color = Color.Black,
            maxLines = 1)
        Box(
            Modifier
                .padding(8.dp)
                .constrainAs(imgUrl) {
                    start.linkTo(tvTitle.start)
                    end.linkTo(tvTitle.end)
                    top.linkTo(tvTitle.bottom)
                    bottom.linkTo(imgCard.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .background(Color.Cyan))
        Button(
            onClick = {},
            Modifier
                .constrainAs(btnOne) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(imgCard.bottom, margin = 16.dp)
                }) {
            Text("Boton 1")
        }
        Button(
            onClick = {},
            Modifier
                .constrainAs(btnTwo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btnOne.bottom, margin = 16.dp)
                }) {
            Text("Boton 2")
        }
        Button(
            onClick = {},
            Modifier
                .constrainAs(btnThree) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btnTwo.bottom, margin = 16.dp)
                }) {
            Text("Boton 3")
        }

        Image(
            painter = painterResource(R.drawable.kotlin_01),
            contentDescription = null,
            Modifier
                .constrainAs(cvAd) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(cvAd.bottom, margin = 8.dp)
                    width = Dimension.fillToConstraints


                }
                //.fillMaxWidth()
                .height(180.dp)
                .background(Color.Magenta))
        Image(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            Modifier
                .constrainAs(btnPlay) {
                    end.linkTo(cvAd.end)
                    bottom.linkTo(cvAd.bottom)
                }
                .clickable {}
                .background(Color.LightGray)
        )
        Image(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            Modifier

                .constrainAs(btnPlay2) {
                    end.linkTo(btnPlay.start)
                    bottom.linkTo(cvAd.bottom)
                }
                .clickable {}
                .background(Color.LightGray))

        ConstraintLayout(
            modifier
                .constrainAs(cGuides) {
                    top.linkTo(btnPlay2.bottom)
                }
                .fillMaxWidth()
                .background(Color.White)) {
            val startGuideLine = createGuidelineFromStart(0.25f)
            val endGuideLine = createGuidelineFromEnd(0.50f)
            val topGuideLine = createGuidelineFromTop(16.dp)
            val bottonGuideLine = createGuidelineFromBottom(32.dp)

            val (txtStart, txtEnd) = createRefs()

            Text(
                "start = 25%, top = 16dp",
                Modifier
                    .constrainAs(txtStart) {
                        start.linkTo(startGuideLine)
                        end.linkTo(parent.end)
                        top.linkTo(topGuideLine)
                        width = Dimension.fillToConstraints
                    }
                    .background(Color.LightGray),
                color = Color.Black
            )
            Text(
                "end = 50%, botton = 16dp",
                Modifier
                    .constrainAs(txtEnd) {
                        start.linkTo(parent.start)
                        end.linkTo(endGuideLine)
                        top.linkTo(txtStart.bottom, margin = 8.dp)
                        bottom.linkTo(bottonGuideLine)
                        width = Dimension.fillToConstraints
                    }
                    .background(Color.Black),
                color = Color.White
            )
        }
    }


}
package com.cursosant.jccomponents

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cursosant.robinson.AntDialog
import com.cursosant.robinson.ContentView
import com.cursosant.robinson.R
import com.cursosant.robinson.ui.theme.JCComponentsTheme
import kotlinx.coroutines.launch

/**
 * Project: JC Components
 * From: com.cursosant.jccomponents
 * Created by: Alain Nicolás Tello
 * On: 10/03/25 at 15:13
 * All rights reserved 2025.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JCComponentsTheme {
                ContentMain {
                    finish()
                }
            }
        }
    }
}

@Composable
fun ContentMain(onFinish: () -> Unit) {
    val context = LocalContext.current
    val colorBar = context.getColor(R.color.purple_300)
    //Snackbar
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    //Menu
    var expandedMenu by remember { mutableStateOf(false) }
    // FloatingActionButton
    var fabPosition by remember { mutableStateOf(FabPosition.Center) }
    var showFab by remember { mutableStateOf(true) }
    //Dialog
    var openDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomAppBar(containerColor = Color(colorBar)) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    val msg = stringResource(R.string.message_success_event)
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar(msg) }
                    }) {
                        Icon(Icons.Filled.Menu,
                            contentDescription = "Menu")
                    }
                }
                Spacer(Modifier.weight(1f, true))
                Box {
                    val byeMsg = stringResource(R.string.message_bye)
                    val exitStr = stringResource(R.string.action_exit)
                    IconButton(onClick = {
                        expandedMenu = true
                    }) {
                        Icon(Icons.Filled.MoreVert,
                            contentDescription = "Options")
                    }

                    DropdownMenu(expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(exitStr)
                            },
                            onClick = {
                                expandedMenu = false
                                Toast.makeText(context, byeMsg, Toast.LENGTH_SHORT).show()
                                onFinish()//finish()
                            })
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = showFab,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(onClick = {
                    if (fabPosition == FabPosition.Center) {
                        fabPosition = FabPosition.End
                    } else {
                        fabPosition = FabPosition.Center
                    }
                }) {//https://mvnrepository.com/artifact/androidx.compose.material/material-icons-extended/1.7.8
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                }
            }
            /*if (showFab) {
                FloatingActionButton(onClick = {
                    if (fabPosition == FabPosition.Center) {
                        fabPosition = FabPosition.End
                    } else {
                        fabPosition = FabPosition.Center
                    }
                }) {//https://mvnrepository.com/artifact/androidx.compose.material/material-icons-extended/1.7.8
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                }
            }*/
        },
        floatingActionButtonPosition = fabPosition) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            val actionGo = stringResource(R.string.action_go)
            val recordMsg = stringResource(R.string.message_record)
            ContentView(Modifier.padding(innerPadding)) { msg, isSwitchChecked, isDialogShow ->
                msg?.let {
                    scope.launch {
                        val result = snackbarHostState
                            .showSnackbar(
                                message = msg,
                                actionLabel = actionGo,
                                duration = SnackbarDuration.Indefinite
                            )
                        when(result){
                            SnackbarResult.ActionPerformed -> {
                                Toast.makeText(context, recordMsg, Toast.LENGTH_SHORT).show()
                            }
                            SnackbarResult.Dismissed -> {}
                        }
                    }
                }

                isSwitchChecked?.let {
                    showFab = isSwitchChecked
                }

                isDialogShow?.let {
                    openDialog = isDialogShow
                }
            }

            if (openDialog) {
                /*MyAlertDialog {
                    openDialog = false
                }*/
                AntDialog(
                    onDismissRequest = {
                        Log.i("CursosANTAG", "Dismiss")
                        openDialog = false
                    },
                    onConfirm = {
                        Log.i("CursosANTAG", "Confirmar")
                        openDialog = false
                    },
                    onCancel = {
                        Log.i("CursosANTAG", "Cancelar")
                        openDialog = false
                    },
                    onNeutral = {
                        Log.i("CursosANTAG", "Saltar")
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JCComponentsTheme {
        ContentMain{}
    }
}
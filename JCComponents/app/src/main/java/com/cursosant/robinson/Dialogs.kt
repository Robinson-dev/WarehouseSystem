package com.cursosant.robinson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.cursosant.robinson.ui.theme.Typography

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
@Preview(showBackground = true)
@Composable
private fun DialogPreview(){
    AntDialog({}, {}, {}, {})
}

@Composable
fun AntDialog(onDismissRequest: () -> Unit,
              onConfirm: () -> Unit,
              onCancel: () -> Unit,
              onNeutral: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest()}) {
        Card(Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.common_padding_default))) {
            Column(Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.common_padding_default)),
                verticalArrangement = Arrangement.Center) {
                Text(stringResource(R.string.dialog_title),
                    style = Typography.headlineMedium)
                Text(stringResource(R.string.dialog_message),
                    style = Typography.bodyMedium)

                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    TextButton(onClick = {
                        onDismissRequest()
                        onNeutral()
                    }) {
                        Text(stringResource(R.string.btn_skip))
                    }

                    Spacer(Modifier.weight(1f))

                    TextButton(onClick = { onCancel() }) {
                        Text(stringResource(R.string.btn_cancel))
                    }
                    TextButton(onClick = { onConfirm() }) {
                        Text(stringResource(R.string.common_ok))
                    }
                }
            }
        }
    }
}

@Composable
fun MyAlertDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        title = {
            Text("Cursos ANT")
        },
        text = {
            Text("Domina Android")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        }
    )
}
package tunanh.test_app

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun Dropdown() {
    val context = LocalContext.current

    var showMenu by remember {
        mutableStateOf(false)
    }

    Box {
        IconButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.tooltip(stringResource(R.string.more))
        ) {
            Icon(Icons.Default.MoreVert, "More options")
        }

        DropdownMenu(
            expanded = showMenu,
            modifier = Modifier.widthIn(min = 150.dp),
            onDismissRequest = { showMenu = false }) {
//            if (BuildConfig.DEBUG) {
//                DropdownMenuItem(
//                    text = { Text(stringResource(R.string.refresh_proxy)) },
//                    onClick = {
//                        showMenu = false
//                        context.startForegroundService(
//                            Intent(
//                                context,
//                                BluetoothService::class.java
//                            ).apply {
//                                action = BluetoothService.ACTION_REGISTER
//                            }
//                        )
//                    }
//                )
//                DropdownMenuItem(
//                    text = { Text(stringResource(R.string.stop_proxy)) },
//                    onClick = {
//                        showMenu = false
//                        context.startService(
//                            Intent(
//                                context,
//                                BluetoothService::class.java
//                            ).apply {
//                                action = BluetoothService.ACTION_STOP
//                            }
//                        )
//                    }
//                )
//            }

            DropdownMenuItem(
                text = { Text(stringResource(R.string.exit)) },
                leadingIcon = { Icon(Icons.Default.Close, null) },
                onClick = {
                    (context as Activity).finishAfterTransition()
                }
            )
        }
    }
}

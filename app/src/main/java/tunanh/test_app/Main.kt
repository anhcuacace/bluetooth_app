package tunanh.test_app

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() = with(viewModel<MainViewModel>()) {

//    Scaffold(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//    topBar = {
//        TopAppBar (
//            title = { Text("")}
//                )
//    }) {
//
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(title = { Text("Card Reader") },
            actions = {
                Dropdown()
            })
    }
}

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

//            DropdownMenuItem(
//                text = { Text(stringResource(R.string.settings)) },
//                leadingIcon = { Icon(Icons.Outlined.Settings, null) },
//                onClick = {
//                    showMenu = false
//                    context.startActivity(Intent(context, SettingsActivity::class.java))
//                }
//            )
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
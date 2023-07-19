package tunanh.test_app.pay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import tunanh.test_app.R
import tunanh.test_app.ui.theme.Test_appTheme

class PayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Test_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2(Modifier.fillMaxSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting2(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.select_type_connection)) },
            actions = {
                IconButton(onClick = { (context as PayActivity).finishAfterTransition() }) {
                    Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "exit")
                }
            })
    }) { padding ->
        with(viewModel<PayViewModel>()) {
            val cardData by cardDataState.collectAsState()
            Column(modifier.padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    listener(context.applicationContext)
                }) {
                    Text(text = "listener")
                }

                Text(
                    text = cardData.cardNumber,
                    style = TextStyle(fontSize = 24.sp)
                )
                Text(
                    text = cardData.expiry,
                    style = TextStyle(fontSize = 16.sp)
                )
                Text(
                    text = cardData.name,
                    style = TextStyle(fontSize = 12.sp)
                )
                if (cardData.cardNumber.isNotEmpty() && cardData.expiry.isNotEmpty()) {

                    TextField(value = "", onValueChange = {

                    })
                    Button(onClick = {

                    }) {
                        Text(text = "pay")
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Test_appTheme {
        Greeting2(Modifier.fillMaxSize())
    }
}
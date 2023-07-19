package tunanh.test_app.pay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
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
            val listenerEnabled by canListener.collectAsState()
            val payEnabled by canPay.collectAsState()
            val message by messageState.collectAsState()
            var amount by remember { mutableStateOf("") }
            rememberCoroutineScope().launch {
                disconnectState.collect {
                    if (it) {
                        if (context is PayActivity) {
                            Toast.makeText(
                                context.applicationContext,
                                "lost connection need reconnect",
                                Toast.LENGTH_LONG
                            ).show()
                            context.finish()
                        }
                    }
                }
            }
            Column(modifier.padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    listener(context.applicationContext)
                }, enabled = listenerEnabled) {
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
                    Text(text = "amount")
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = amount,
                        placeholder = {
                            Text(text = "0.00")
                        },
                        onValueChange = { newText ->
                            amount = newText
                            canPay.value = newText.isNotEmpty() && newText.toDouble() > 0
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Button(onClick = {
                        pay(amount)
                    }, enabled = payEnabled) {
                        Text(text = "pay")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = message)
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
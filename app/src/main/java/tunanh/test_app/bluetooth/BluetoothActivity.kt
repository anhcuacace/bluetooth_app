package tunanh.test_app.bluetooth

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import tunanh.test_app.ComposableLifecycle
import tunanh.test_app.NavGraph
import tunanh.test_app.RequiresBluetoothPermission
import tunanh.test_app.ui.theme.Test_appTheme

val LocalController = staticCompositionLocalOf<BluetoothController> {
    error("No BluetoothController provided")
}

class BluetoothActivity : ComponentActivity() {

    private var bluetoothController: BluetoothController? by mutableStateOf(null)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? BluetoothService.LocalBinder
            bluetoothController = binder?.getController()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bluetoothController = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Surface(Modifier.fillMaxSize()) {
                Test_appTheme {
                    RequiresBluetoothPermission {
                        bluetoothController?.let {
                            CompositionLocalProvider(LocalController provides it) {
                                NavGraph()
                            }
                        }

                        ComposableLifecycle(LocalLifecycleOwner.current) { _, event ->
                            when (event) {
                                Lifecycle.Event.ON_CREATE -> {
                                    // Start and bind bluetooth service
                                    Intent(
                                        this@BluetoothActivity,
                                        BluetoothService::class.java
                                    ).let {
                                        startForegroundService(it)
                                        bindService(it, serviceConnection, BIND_AUTO_CREATE)
                                    }
                                }

                                Lifecycle.Event.ON_DESTROY -> {
                                    // Don't stop service if activity is being recreated due to a configuration change
                                    if (!isChangingConfigurations) {
                                        // Unbind and stop bluetooth service
                                        unbindService(serviceConnection)
                                        stopService(
                                            Intent(
                                                this@BluetoothActivity,
                                                BluetoothService::class.java
                                            )
                                        )
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }

    }
}


package tunanh.test_app

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
import tunanh.test_app.bluetooth.BluetoothController
import tunanh.test_app.bluetooth.BluetoothService

val LocalController = staticCompositionLocalOf<BluetoothController> {
    error("No BluetoothController provided")
}

class MainActivity : ComponentActivity() {
    //    private val permission =
//        if (Utils.isAndroidS())
//            arrayOf(
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT,
//                Manifest.permission.BLUETOOTH_ADVERTISE,
//            ) else arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
//
//    private val bluetoothScanner: BluetoothScanner by lazy { BluetoothScanner(this) }
//    private var permissionGrand = false
//
//    private val requestBluetooth =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                bluetoothScan()
//                permissionGrand = true
//            }
//        }
//
//    private val requestMultiplePermissions =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            if (permissions.values.all { it }) {
//                bluetoothScan()
//                permissionGrand = true
//            }
//
//        }
//
//    private fun bluetoothScan() {
//        setContent {
//            val devicesState = remember { mutableStateListOf<BluetoothDevice>() }
//
//            Column {
//                Button(
//                    onClick = { bluetoothScanner.startScan { devicesState.addAll(it) } },
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text("Start Scan")
//                }
//                BluetoothScanScreen(devices = devicesState)
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (!Utils.allPermissionsGranted(permission.toList(), this)) {
//            if (Utils.isAndroidS()) {
//                requestMultiplePermissions.launch(permission)
//            } else {
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                requestBluetooth.launch(enableBtIntent)
//            }
//        } else {
//            bluetoothScan()
//            permissionGrand = true
//        }
//
//    }
//
//    override fun onDestroy() {
//        if (permissionGrand)
//            bluetoothScanner.stopScan()
//        super.onDestroy()
//    }
//    @SuppressLint("MissingPermission")
//    @Composable
//    fun BluetoothScanScreen(devices: List<BluetoothDevice>) {
//        Column {
//            Text("Bluetooth Devices:")
//            devices.forEach { device ->
//                Text(device.name)
//            }
//        }
//    }
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
                                Intent(this@MainActivity, BluetoothService::class.java).let {
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
                                            this@MainActivity,
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


//@Composable
//fun Greeting2(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    Test_appTheme {
//        Greeting2("Android")
//    }
//}
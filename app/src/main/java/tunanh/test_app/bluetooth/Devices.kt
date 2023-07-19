package tunanh.test_app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import tunanh.test_app.DeviceInfo
import tunanh.test_app.DeviceInfoDialog
import tunanh.test_app.DevicesViewModel
import tunanh.test_app.PreferenceStore
import tunanh.test_app.R
import tunanh.test_app.SystemBroadcastReceiver
import tunanh.test_app.Utils
import tunanh.test_app.pay.PayActivity
import tunanh.test_app.pre.ConnectIdTech
import tunanh.test_app.rememberPreferenceDefault
import tunanh.test_app.tooltip
import tunanh.test_app.ui.ConfirmDialog
import tunanh.test_app.ui.DialogState
import tunanh.test_app.ui.LoadingDialog
import tunanh.test_app.ui.rememberDialogState
import tunanh.test_app.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Devices() = with(viewModel<DevicesViewModel>()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val controller = LocalController.current
    val context = LocalContext.current
//    val navigation = LocalNavigation.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.devices)) },
                actions = {

                    IconButton(
                        onClick = {
                            if (!isScanning) refresh(controller)
                            else controller.cancelScan()
                        },
                        modifier = Modifier.tooltip(
                            if (isScanning) stringResource(R.string.cancel)
                            else stringResource(R.string.refresh)
                        )
                    ) {
                        Icon(
                            if (isScanning) Icons.Default.Close else Icons.Default.Refresh,
                            "Refresh"
                        )
                    }
                    IconButton(onClick = { (context as BluetoothActivity).finishAfterTransition() }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "exit")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) { padding ->
        Box(Modifier.padding(padding)) {
            DeviceContent()
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MissingPermission")
@Composable
fun DevicesViewModel.DeviceContent() {
    val dialogState = rememberDialogState()
    val controller = LocalController.current

    DisposableEffect(controller) {
        isScanning = controller.isScanning
        isBluetoothEnabled = controller.bluetoothEnabled

        if (pairedDevices.isEmpty()) {
            pairedDevices.addAll(controller.pairedDevices)
        }

        val listener = controller.registerListener { _, state ->
//            dialogState.openState = when (state) {
//                BluetoothProfile.STATE_CONNECTING -> true
//                BluetoothProfile.STATE_DISCONNECTING -> true
//                else -> false // Only close if connected or fully disconnected
//            }
        }

        onDispose {
            controller.unregisterListener(listener)
        }
    }

    LoadingDialog(
        dialogState,
        stringResource(R.string.connecting),
        stringResource(R.string.connect_help)
    )

    BroadcastListener()

    val pullRefreshState =
        rememberPullRefreshState(isRefreshing, { refresh(controller) })

    Box(Modifier.pullRefresh(pullRefreshState)) {
        DeviceList(dialogState)

        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}


@Composable
fun DevicesViewModel.BroadcastListener() {
    SystemBroadcastReceiver(BluetoothAdapter.ACTION_STATE_CHANGED) { intent ->
        isBluetoothEnabled =
            intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON
    }

    SystemBroadcastReceiver(BluetoothAdapter.ACTION_DISCOVERY_STARTED) {
        isScanning = true
        foundDevices.clear()
    }

    SystemBroadcastReceiver(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
        isScanning = false
    }

    SystemBroadcastReceiver(BluetoothDevice.ACTION_FOUND) {
        if (Utils.isTIRAMISU()) {
            it?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        } else {
            @Suppress("DEPRECATION") it?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }?.let { dev ->
            if (!foundDevices.contains(dev)) {
                foundDevices.add(dev)
            }
        }
    }
}

//private val mSwiperControllerManager by lazy {  SwiperControllerManager().getInstance()}
//
//private val swiperListener = object : SwiperControllerListener {
//    override fun onTokenGenerated(p0: CCConsumerAccount?, p1: CCConsumerError?) {
//        Timber.e("onTokenGenerated " + p0?.name)
//    }
//
//    override fun onError(p0: SwiperError?) {
//        Timber.e("onError " + p0?.exceptionMessage)
//    }
//
//    override fun onSwiperReadyForCard() {
//        Timber.e("onSwiperReadyForCard")
//    }
//
//    override fun onSwiperConnected() {
//        Timber.e("onSwiperConnected")
//    }
//
//    override fun onSwiperDisconnected() {
//        Timber.e("onSwiperDisconnected")
//    }
//
//    override fun onBatteryState(p0: BatteryState?) {
//        Timber.e("onBatteryState")
//    }
//
//    override fun onStartTokenGeneration() {
//        Timber.e("onStartTokenGeneration")
//    }
//
//    override fun onLogUpdate(p0: String?) {
//        Timber.e("onLogUpdate $p0")
//    }
//
//    override fun onDeviceConfigurationUpdate(p0: String?) {
//        Timber.e("onDeviceConfigurationUpdate $p0")
//    }
//
//    override fun onConfigurationProgressUpdate(p0: Double) {
//        Timber.e("onConfigurationProgressUpdate $p0")
//    }
//
//    override fun onConfigurationComplete(p0: Boolean) {
//        Timber.e("onConfigurationComplete $p0")
//    }
//
//    override fun onTimeout() {
//        Timber.e("onTimeout")
//    }
//
//    override fun onLCDDisplayUpdate(p0: String?) {
//        Timber.e("onLCDDisplayUpdate $p0")
//    }
//
//    override fun onRemoveCardRequested() {
//        Timber.e("onRemoveCardRequested")
//    }
//
//    override fun onCardRemoved() {
//        Timber.e("onCardRemoved")
//    }
//
//    override fun onDeviceBusy() {
//        Timber.e("onDeviceBusy")
//    }
//
//}
private fun gotoPayActivity(context: Context) {
    context.apply {
        if (this is BluetoothActivity) {
            startActivity(Intent(this, PayActivity::class.java))
        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun DevicesViewModel.DeviceList(dialogState: DialogState) {
    val showUnnamed by rememberPreferenceDefault(PreferenceStore.SHOW_UNNAMED)
    val context = LocalContext.current
    val connect = ConnectIdTech.getInstance()


    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(12.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!isBluetoothEnabled) {
            item {
                BluetoothDisabledCard()
            }
        } else {
            item {
                Text(
                    stringResource(R.string.scanned_devices),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            if (isScanning) {
                item {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
            }

            // Filter out unnamed devices depending on preference
            with(foundDevices.filter { showUnnamed || it.name != null }) {
                if (isEmpty() || !isBluetoothEnabled) {
                    item {
//                        RequireLocationPermission {
                        if (!isScanning) {
                            Text(stringResource(R.string.swipe_refresh))
                        }
//                        }
                    }
                } else {
                    items(this) { d ->
                        runCatching {
                            DeviceCard(d, {
//                                onConnect(d)
                                dialogState.openState = true
                                connect.connectBlueTooth(d.address, context.applicationContext)
                            }, dialogState)
                        }.onFailure {
                            Timber.tag("DeviceList").e(it, "Failed to get device info")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.paired_devices),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            if (pairedDevices.isEmpty() || !isBluetoothEnabled) {
                item {
                    Text(stringResource(R.string.no_paired_devices))
                }
            } else {
                items(pairedDevices) {
                    runCatching {
                        DeviceCard(it, onClick = {
                            connect.connectBlueTooth(it.address, context.applicationContext)
                            dialogState.openState = true
//                            mSwiperControllerManager.setSwiperType(SwiperType.IDTech)
                        }, dialogState)
                    }.onFailure {
                        Timber.e(it, "Failed to get device info")
                    }
                }
            }
        }
    }
}


@SuppressLint("MissingPermission", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    device: BluetoothDevice,
    onClick: () -> Unit,
    dialogState: DialogState? = null,
) {
    val infoDialog = rememberDialogState()
    val confirmDialog = rememberDialogState()
    val context = LocalContext.current
    val connect = ConnectIdTech.getInstance()
    //    val controller= LocalController.current
    rememberCoroutineScope().launch {
        connect.availableConnect.onEach {
            Timber.e(dialogState?.openState.toString())
            Timber.e((it && dialogState?.openState == true).toString())
            if (it && dialogState?.openState == true) {
                Timber.e("go")
                dialogState.openState = false
                gotoPayActivity(context)
            }
        }.launchIn(this)
        connect.connectBlueToothState.onEach { rc ->
            if (dialogState?.openState == true) {
                if (rc != 0) {
                    when (rc) {
                        1 -> Toast.makeText(context, "Invalid DEVICE_TYPE", Toast.LENGTH_SHORT)
                            .show()

                        2 -> Toast.makeText(
                            context,
                            "Bluetooth LE is not supported on this device",
                            Toast.LENGTH_SHORT
                        ).show()

                        3 -> Toast.makeText(
                            context,
                            "Bluetooth LE is not available",
                            Toast.LENGTH_SHORT
                        ).show()

                        4 -> Toast.makeText(
                            context,
                            "Bluetooth LE is not enabled",
                            Toast.LENGTH_SHORT
                        ).show()

                        5 -> Toast.makeText(
                            context,
                            "Device not paired. Please pair first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
//                gotoPayActivity(context)

//                    Toast.makeText(
//                        context,
//                        "Failed. Please disconnect first.",
//                        Toast.LENGTH_SHORT
//                    ).show()

                }
            }
        }.launchIn(this)

    }


    val deviceName = device.name ?: ""

    ElevatedCard(
        onClick,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
//        controller.connectApi(device)
        Row(Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Icon(
                    when (DeviceInfo.deviceClassString(device.bluetoothClass.majorDeviceClass)) {
                        "PHONE" -> Icons.Default.Smartphone
                        "AUDIO_VIDEO" -> Icons.Default.Headphones
                        "COMPUTER" -> Icons.Default.Computer
                        "PERIPHERAL" -> Icons.Default.Keyboard
                        else -> Icons.Default.Bluetooth
                    },
                    "Type"
                )
            }
            Column(
                Modifier
                    .padding(4.dp)
                    .weight(1f)
            ) {
                Text(deviceName)
                Text(
                    device.address,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = Typography.labelSmall,
                )
            }
            Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                if (device.bondState == BluetoothDevice.BOND_BONDED) {
                    DeviceDropdown(
                        onConnect = onClick,
                        onInfo = { infoDialog.open() },
                        onRemove = { confirmDialog.open() }
                    ) {
                        Icon(Icons.Default.MoreVert, "More options for $deviceName")
                    }
                } else {
                    Icon(Icons.Default.PlayArrow, "Connect")
                }
            }
        }
    }

    DeviceInfoDialog(infoDialog, device)

    ConfirmDialog(confirmDialog, stringResource(R.string.unpair_device, deviceName), onConfirm = {
        device.removeBond()
        close()
    }) {
        Text(stringResource(R.string.unpair_desc))
    }
}


@SuppressLint("MissingPermission")
@Composable
fun BluetoothDisabledCard() {
    val context = LocalContext.current

    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.BluetoothDisabled, null, Modifier.size(64.dp))

            Text(stringResource(R.string.bluetooth_disabled), style = Typography.headlineMedium)
            Spacer(Modifier.height(8.dp))

            Text(stringResource(R.string.enable_bluetooth), style = Typography.bodyMedium)
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)) },
            ) {
                Text(stringResource(R.string.enable_bluetooth_btn))
            }
        }
    }
}


@Composable
fun DeviceDropdown(
    onConnect: () -> Unit = {},
    onInfo: () -> Unit = {},
    onRemove: () -> Unit = {},
    icon: @Composable () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = { showMenu = true },
        modifier = Modifier.tooltip(stringResource(R.string.more)),
        content = icon
    )

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
    ) {
        DropdownMenuItem(
            onClick = {
                showMenu = false
                onConnect()
            },
            text = { Text(stringResource(R.string.connect)) }
        )
        DropdownMenuItem(
            onClick = {
                showMenu = false
                onInfo()
            },
            text = { Text(stringResource(R.string.info)) }
        )
        DropdownMenuItem(
            onClick = {
                showMenu = false
                onRemove()
            },
            text = { Text(stringResource(R.string.unpair)) }
        )
    }
}

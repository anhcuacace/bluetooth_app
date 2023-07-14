package tunanh.test_app.pre

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dbconnection.dblibrarybeta.ProfileManager
import com.idtechproducts.device.IDTEMVData
import com.idtechproducts.device.IDTMSRData
import com.idtechproducts.device.IDT_VP3300
import com.idtechproducts.device.OnReceiverListener
import com.idtechproducts.device.OnReceiverListenerPINRequest
import com.idtechproducts.device.ReaderInfo.DEVICE_TYPE
import com.idtechproducts.device.StructConfigParameters
import com.idtechproducts.device.audiojack.tools.FirmwareUpdateTool
import com.idtechproducts.device.audiojack.tools.FirmwareUpdateToolMsg
import timber.log.Timber

class ConnectViewModel : ViewModel(), OnReceiverListener, OnReceiverListenerPINRequest,
    FirmwareUpdateToolMsg {
    private var device: IDT_VP3300? = null
    private var fwTool: FirmwareUpdateTool? = null
    private val profileManager: ProfileManager? = null

    fun connectUsb(context: Context) {
        if (device == null) {
            initializeReader(context)
        }
        if (device!!.device_setDeviceType(DEVICE_TYPE.DEVICE_VP3300_BT_USB)) Toast.makeText(
            context,
            "VP3300 Bluetooth (USB) is selected",
            Toast.LENGTH_SHORT
        ).show() else Toast.makeText(
            context,
            "Failed. Please disconnect first.",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun initializeReader(context: Context) {
        if (device != null) {
            releaseSDK()
        }
        device = IDT_VP3300(this, this, context)
        profileManager?.doGet()
        Toast.makeText(context, "get started", Toast.LENGTH_LONG).show()
        device!!.log_setVerboseLoggingEnable(true)
        fwTool = FirmwareUpdateTool(this, context)

    }

    private fun releaseSDK() {
        if (device != null) {
            if (device!!.device_getDeviceType() != DEVICE_TYPE.DEVICE_VP3300_COM) device!!.unregisterListen()
            device!!.release()
            //				device = null;
        }
    }

    override fun swipeMSRData(p0: IDTMSRData?) {

    }

    override fun lcdDisplay(p0: Int, p1: Array<out String>?, p2: Int) {

    }

    override fun lcdDisplay(p0: Int, p1: Array<out String>?, p2: Int, p3: ByteArray?, p4: Byte) {

    }

    override fun ctlsEvent(p0: Byte, p1: Byte, p2: Byte) {

    }

    override fun emvTransactionData(p0: IDTEMVData?) {

    }

    override fun deviceConnected() {
        Timber.e("connected")
    }

    override fun deviceDisconnected() {

    }

    override fun timeout(p0: Int) {

    }

    override fun autoConfigCompleted(p0: StructConfigParameters?) {

    }

    override fun autoConfigProgress(p0: Int) {

    }

    override fun msgRKICompleted(p0: String?) {

    }

    override fun ICCNotifyInfo(p0: ByteArray?, p1: String?) {

    }

    override fun msgBatteryLow() {

    }

    override fun LoadXMLConfigFailureInfo(p0: Int, p1: String?) {

    }

    override fun msgToConnectDevice() {

    }

    override fun msgAudioVolumeAjustFailed() {

    }

    override fun dataInOutMonitor(p0: ByteArray?, p1: Boolean) {

    }

    override fun pinRequest(
        p0: Int,
        p1: ByteArray?,
        p2: ByteArray?,
        p3: Int,
        p4: Int,
        p5: String?,
    ) {

    }

    override fun onReceiveMsgUpdateFirmwareProgress(p0: Int) {

    }

    override fun onReceiveMsgUpdateFirmwareResult(p0: Int) {

    }

    override fun onReceiveMsgChallengeResult(p0: Int, p1: ByteArray?) {

    }
}
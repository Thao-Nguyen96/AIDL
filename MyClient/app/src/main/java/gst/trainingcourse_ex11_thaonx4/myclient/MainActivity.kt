package gst.trainingcourse_ex11_thaonx4.myclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.view.View
import android.widget.Toast
import gst.trainingcourse_ex11_thaonx4.myclient.databinding.ActivityMainBinding
import gst.trainingcourse_ex11_thaonx4.myserver.IIPCExample
import gst.trainingcourse_ex11_thaonx4.myserver.type.PackageInfo

class MainActivity : AppCompatActivity(), View.OnClickListener, ServiceConnection {

    companion object {
        const val AIDL_ACTION = "gst.trainingcourse_ex11_thaonx4.myserver.action.AIDL_ACTION"
        const val EMPTY_STRING = ""
    }

    private lateinit var binding: ActivityMainBinding

    private var connected = false
    var iRemoteService: IIPCExample? = null

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        iRemoteService = IIPCExample.Stub.asInterface(service)

        binding.txtServerPid.text = iRemoteService?.pid.toString()
        binding.txtServerConnectionCount.text = iRemoteService?.connectionCount.toString()

        iRemoteService?.setDisplayedValue(
            this.packageName,
            Process.myPid(),
            binding.edtClientData.text.toString())

//        iRemoteService?.setDisplayedValueWithObject(
//            PackageInfo(
//                this.packageName,
//                Process.myPid(),
//                binding.edtClientData.text.toString()
//            )
//        )
        connected = true
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Toast.makeText(this, "IPC server has disconnected unexpectedly", Toast.LENGTH_LONG).show()
        iRemoteService = null
        connected = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConnect.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        connected = if (connected) {
            disconnectToRemoteService()
            binding.txtServerPid.text = EMPTY_STRING
            binding.txtServerConnectionCount.text = EMPTY_STRING
            binding.btnConnect.text = getString(R.string.connect)
            binding.linearLayoutClientInfo.visibility = View.INVISIBLE
            false
        } else {
            connectToRemoteService()
            binding.linearLayoutClientInfo.visibility = View.VISIBLE
            binding.btnConnect.text = getString(R.string.disconnect)
            true
        }
    }

    private fun disconnectToRemoteService() {
        if (connected) {
            unbindService(this)
        }
    }

    private fun connectToRemoteService() {

        val intent = Intent(AIDL_ACTION)
        val pack = IIPCExample::class.java.`package`

        intent.setPackage(pack?.name)
        bindService(intent, this, Context.BIND_AUTO_CREATE)

    }

}
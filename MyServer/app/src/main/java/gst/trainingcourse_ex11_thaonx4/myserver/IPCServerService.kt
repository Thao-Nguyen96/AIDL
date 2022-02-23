package gst.trainingcourse_ex11_thaonx4.myserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.text.TextUtils
import android.util.Log
import gst.trainingcourse_ex11_thaonx4.myserver.type.PackageInfo

class IPCServerService : Service() {

    companion object {
        var connectionCount: Int = 0
        const val NOT_SENT = "Not sent!"
        const val AIDL_ACTION = "gst.trainingcourse_ex11_thaonx4.myserver.action.AIDL_ACTION"
    }


    private var aidlIBinder = object : IIPCExample.Stub() {
        override fun getPid(): Int = Process.myPid()

        override fun getConnectionCount(): Int = IPCServerService.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, data: String?) {
            println("setDisplayedValue")

            val clientData =
                if (data == null || TextUtils.isEmpty(data)) NOT_SENT else data

            //val current = resources.configuration.locale

            RecentClient.client = Client(
                packageName ?: NOT_SENT,
                pid.toString(),
                clientData,
                "AIDL"
            )
        }

        override fun setDisplayedValueWithObject(packageInfo: PackageInfo?) {
            println("setDisplayedValueWithObject")

            var count = 0

            Thread{
                for (i in 0..100){
                    count++
                    Log.d("xuanthao", count.toString())
                }

               //Log.d("xuanthao", current.toString())
            }.start()

            RecentClient.client = Client(
                packageInfo?.packageName ?: NOT_SENT,
                packageInfo?.pid.toString(),
                packageInfo?.data,
                "AIDL"
                )
        }

    }


    override fun onBind(p0: Intent?): IBinder? {
        connectionCount++

        return when (p0?.action) {
            AIDL_ACTION -> aidlIBinder
            else -> null
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        RecentClient.client = null
        return super.onUnbind(intent)
    }
}
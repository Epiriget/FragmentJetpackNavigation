package com.example.fragmentjetpacknavigation

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class FibonacciService : Service() {
    private var serviceLooper: Looper? = null;
    private var serviceHandler: ServiceHandler? = null;

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            try {
                val res = fibCount(msg.arg2.toLong())
                sendMessageToFragment(res)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
        private fun fibCount(i:Long): Long {
            if(i <= 1) return i
            return fibCount(i - 1) + fibCount(i - 2)
        }

        private fun sendMessageToFragment(result:Long) {
            Intent(FibonacciService::class.java.simpleName).apply {
                putExtra(FirstFragment.SERVICE_RESULT, result)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(this)
            }
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val arg = intent?.getLongExtra(FirstFragment.SERVICE_START_ARGS_KEY, 0L) ?: 0L
        Toast.makeText(this, "service starting with $arg", Toast.LENGTH_SHORT).show()

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            msg.arg2 = arg.toInt()
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}

package com.geekstudio.android14test.ui.intent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TestBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, inent: Intent?) {
        Log.d("TestBroadcastReceiver", "intent action = ${inent?.action}")
    }
}
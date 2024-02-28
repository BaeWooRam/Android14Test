package com.geekstudio.android14test.ui.intent

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geekstudio.android14test.MainActivity
import com.geekstudio.android14test.TestButton
import com.geekstudio.android14test.ui.theme.Android14TestTheme

class IntentTestActivity : ComponentActivity() {
    private val localBroadcastManager = TestLocalBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Android14TestTheme {
                val scrollState = rememberScrollState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Text(text = "BroadcastReceiver")
                        TestButton(onclick = { clickTestButton1() }, name = "명시적(타겟 BR)")
                        TestButton(onclick = { clickTestButton1_1() }, name = "명시적(Local BR)")
                        TestButton(onclick = { clickTestButton2() }, name = "암시적")
                        TestButton(onclick = { clickTestButton3() }, name = "암시적 + 패키지명")
                        Text(text = "Service")
                        TestButton(onclick = { clickTestButton4() }, name = "명시적")
                        TestButton(onclick = { clickTestButton5() }, name = "암시적")
                        TestButton(onclick = { clickTestButton6() }, name = "암시적 + 패키지명")
                        Text(text = "그 밖에")
                        TestButton(onclick = { clickTestBluetoothSettingButton() }, name = "Bluetooth Setting 이동")
                        TestButton(onclick = { clickTestBluetoothEnableButton() }, name = "Bluetooth Enable 실행")
                        TestButton(onclick = { clickTestMainButton() }, name = "Main Category : LAUNCHER 실행")
                        TestButton(onclick = { clickTestMainHomeButton() }, name = "Main Category : HOME 실행")
                        TestButton(onclick = { clickTestTelButton() }, name = "TEL 01092055472 실행")
                        TestButton(onclick = { clickTestSendMessageButton() }, name = "Send Message 실행")
                        TestButton(onclick = { clickTestPickImageButton() }, name = "Pick Image 실행")
                    }
                }
            }
        }


    }

    override fun onStart() {
        super.onStart()
        registerLocalBroadcastManager()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiverLocalBroadcastManager()
    }

    private fun registerLocalBroadcastManager() {
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastManager, IntentFilter("android.intent.action.TEST"))
    }

    private fun unregisterReceiverLocalBroadcastManager() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastManager)
    }

    private fun clickTestButton1() {
        val intent = Intent(this, TestBroadcastReceiver::class.java).apply {
            action = "android.intent.action.TEST"
        }
        sendBroadcast(intent)
    }

    private fun clickTestButton1_1() {
        val intent = Intent(this, BroadcastReceiver::class.java).apply {
            action = "android.intent.action.TEST"
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun clickTestButton2() {
        val intent = Intent().apply {
            action = "android.intent.action.TEST"
        }
        sendBroadcast(intent)
    }

    private fun clickTestButton3() {
        val intent = Intent().apply {
            action = "android.intent.action.TEST"
            setPackage("com.geekstudio.android14test")
        }
        sendBroadcast(intent)
    }

    private fun clickTestButton4() {
        val intent = Intent(this, TestService::class.java).apply {
            action = "android.intent.action.TEST"
        }
        startService(intent)
    }

    private fun clickTestButton5() {
        val intent = Intent().apply {
            action = "android.intent.action.TEST"
        }
        startService(intent)
    }

    private fun clickTestButton6() {
        val intent = Intent().apply {
            action = "android.intent.action.TEST"
            setPackage("com.geekstudio.android14test")
        }
        startService(intent)
    }

    private fun clickTestBluetoothSettingButton() {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        startActivity(intent)
    }

    private val enableBluetoothStartForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            Log.d("IntentTestActivity", "enableBluetoothStartForResult selectedImageUri = $selectedImageUri")
        }
    }

    private fun clickTestBluetoothEnableButton() {
        val bleEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Bluetooth 권한 체크해주세요. BLUETOOTH_CONNECT", Toast.LENGTH_SHORT)
                .show()
            return
        }
        enableBluetoothStartForResult.launch(bleEnableIntent)
    }

    private fun clickTestMainButton() {
        val className = MainActivity::class.java.canonicalName ?: return
        Log.d("IntentTestActivity", "clickTestMainButton className = $className")
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            component = ComponentName(packageName, className)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun clickTestMainHomeButton() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        startActivity(intent)
    }

    private fun clickTestTelButton() {
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.data = Uri.parse("tel:01092055472")
        startActivity(intent)
    }

    private fun clickTestSendMessageButton() {
        val message = "테스트"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private val pickImageStartForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            Log.d("IntentTestActivity", "pickImageStartForResult selectedImageUri = $selectedImageUri")
        }
    }

    private fun clickTestPickImageButton() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        pickImageStartForResult.launch(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action
        Log.d("TestActivity", "intent action = $action")
    }
}
package com.geekstudio.android14test.ui.file

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.geekstudio.android14test.TestButton
import com.geekstudio.android14test.ui.theme.Android14TestTheme
import java.io.File
import java.io.FileOutputStream

class FileTestActivity : ComponentActivity() {
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
                        TestButton(onclick = { fileCopyTestButton("test.bmp") }, name = "DB ReadOnly 테스트")
                    }
                }
            }
        }
    }

    private fun fileCopyTestButton(fileName: String) {
        val buffer = ByteArray(1024)
        var read: Int

        runCatching {
            val inputStream = assets.open(fileName)
            val outputFile = File(getExternalFilesDir(null), fileName)
            Log.d("FileTestActivity","outputFile exists = ${outputFile.exists()}, size = ${outputFile.length()}")
            outputFile.setWritable(true)

            //Android 14 - 더 안전한 동적 코드 로드
            Log.d("FileTestActivity","outputFile before canWrite = ${outputFile.canWrite()}, canRead = ${outputFile.canRead()}")
            FileOutputStream(outputFile).use { out ->
                outputFile.setReadOnly()
                Log.d("FileTestActivity","outputFile after canWrite = ${outputFile.canWrite()}, canRead = ${outputFile.canRead()}")
                while (inputStream.read(buffer).also { data -> read = data } != -1) {
                    out.write(buffer, 0, read)
                }
            }

            return@runCatching outputFile
        }.onSuccess { outputFile ->
            Toast.makeText(this, "File Copy 성공했습니다. size = ${outputFile.length()}", Toast.LENGTH_SHORT).show()
            outputFile.delete()
        }.onFailure {
            it.printStackTrace()
            Toast.makeText(this, "File Copy 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
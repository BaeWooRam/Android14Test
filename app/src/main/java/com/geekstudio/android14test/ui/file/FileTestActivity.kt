package com.geekstudio.android14test.ui.file

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.geekstudio.android14test.R
import com.geekstudio.android14test.TestButton
import com.geekstudio.android14test.ui.theme.Android14TestTheme
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


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
                        TestButton(
                            onclick = { fileCopyTestButton("test.bmp") },
                            name = "DB ReadOnly 테스트"
                        )
                        TestButton(onclick = { checkRootFiles() }, name = "checkRootFiles 테스트")
                        TestButton(onclick = { initAssetFile() }, name = "initAssetFile 테스트")
                        TestButton(onclick = { createContentResolverFile() }, name = "createContentResolverFile 테스트")
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
            Log.d(
                "FileTestActivity",
                "outputFile exists = ${outputFile.exists()}, size = ${outputFile.length()}"
            )
            outputFile.setWritable(true)

            //Android 14 - 더 안전한 동적 코드 로드
            Log.d(
                "FileTestActivity",
                "outputFile before canWrite = ${outputFile.canWrite()}, canRead = ${outputFile.canRead()}"
            )
            FileOutputStream(outputFile).use { out ->
                outputFile.setReadOnly()
                Log.d(
                    "FileTestActivity",
                    "outputFile after canWrite = ${outputFile.canWrite()}, canRead = ${outputFile.canRead()}"
                )
                while (inputStream.read(buffer).also { data -> read = data } != -1) {
                    out.write(buffer, 0, read)
                }
            }

            return@runCatching outputFile
        }.onSuccess { outputFile ->
            Toast.makeText(
                this,
                "File Copy 성공했습니다. size = ${outputFile.length()}",
                Toast.LENGTH_SHORT
            ).show()
            outputFile.delete()
        }.onFailure {
            it.printStackTrace()
            Toast.makeText(this, "File Copy 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkRootFiles(): Boolean {
        val rootFiles = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/system/usr/we-need-root/",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/su/bin",
            "/system/xbin/daemonsu",
            "/sdcard/DCIM/Screenshots/Calendar.jpg"
        )

        for (path in rootFiles) {
            kotlin.runCatching {
                File(path).exists()
            }.onSuccess { isExists ->
                Log.d(
                    this@FileTestActivity.javaClass.simpleName,
                    "checkRootFiles 성공했습니다. isExists = $isExists, path = $path"
                )
            }.onFailure { e ->
                Log.d(
                    this@FileTestActivity.javaClass.simpleName,
                    "checkRootFiles 실패했습니다. e = ${e.message}"
                )
            }
        }
        return false
    }

    private fun initAssetFile() {
        kotlin.runCatching {
            resources.openRawResourceFd(R.raw.test1)
        }.onSuccess { file ->
            Log.d(
                this@FileTestActivity.javaClass.simpleName,
                "initAssetFile 성공했습니다. length = ${file.length}"
            )
        }.onFailure { e ->
            Log.d(
                this@FileTestActivity.javaClass.simpleName,
                "initAssetFile 실패했습니다. e = ${e.message}"
            )
        }
    }

    private fun createContentResolverFile(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Test.jpg")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Test.jpg")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//        values.put(MediaStore.Images.Media.DATA, "/sdcard/DCIM/Screenshots")
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val fileForlder: String =
                Environment.DIRECTORY_MOVIES + "/Test/Records/" + String.format(
                    "%tY",
                    date
                ) + String.format("%tm", date) + String.format("%td", date)
            values.put(MediaStore.Video.Media.RELATIVE_PATH, fileForlder)
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
        }*/
        val uri: Uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ) ?: return

        try {
            val fos: OutputStream? = contentResolver.openOutputStream(uri)
            fos?.flush()
            fos?.close()
            val file = File("/sdcard/DCIM/Screenshots/Test.jpg")
            if (file.exists()) {
                file.delete()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
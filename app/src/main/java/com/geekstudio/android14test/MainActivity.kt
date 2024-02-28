package com.geekstudio.android14test

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.geekstudio.android14test.ui.file.FileTestActivity
import com.geekstudio.android14test.ui.intent.IntentTestActivity
import com.geekstudio.android14test.ui.theme.Android14TestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android14TestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TestButton(onclick = { moveToIntentTest() }, name = "암시적 인텐트와 대기 중인 인텐트 제한사항 체크")
                        TestButton(onclick = { moveToFileTest() }, name = "더 안전한 동적 코드 로드 사용")
                    }
                }
            }
        }
    }

    private fun moveToIntentTest() {
        startActivity(Intent(this, IntentTestActivity::class.java))
    }

    private fun moveToFileTest() {
        startActivity(Intent(this, FileTestActivity::class.java))
    }
}

@Composable
fun TestButton(onclick: () -> Unit, name: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onclick,
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Text(text = name)
    }
}

@Preview(showBackground = true)
@Composable
fun TestButtonPreview() {
    Android14TestTheme {
        TestButton(onclick = {}, name = "테스트")
    }
}
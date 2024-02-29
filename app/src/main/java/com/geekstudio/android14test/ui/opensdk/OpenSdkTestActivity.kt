package com.geekstudio.android14test.ui.opensdk

import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import com.geekstudio.android14test.BuildConfig
import com.geekstudio.android14test.TestButton
import com.geekstudio.android14test.ui.theme.Android14TestTheme
import java.util.UUID
import java.util.regex.Pattern


class OpenSdkTestActivity : ComponentActivity() {
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
                        TestButton(onclick = { fromStringTestButton(BuildConfig.TEST_UUID) }, name = "UUID 테스트")
                        TestButton(onclick = { emailPatternMatchButton("QWE123SDF@naver.com") }, name = "Email Pattern 테스트")
                        TestButton(onclick = { nicknamePatternMatchButton("테스트닉네임") }, name = "NickName Pattern 테스트")
                        TestButton(onclick = { carNoPatternMatchButton("sdfasdfasdfasd00가0000") }, name = "CarNo 테스트")
                    }
                }
            }
        }
    }

    private fun fromStringTestButton(stringUuid: String) {
        val uuid = UUID.fromString(stringUuid)
        Toast.makeText(this, "fromStringTestButton uuid = $uuid", Toast.LENGTH_SHORT).show()
    }

    private fun emailPatternMatchButton(userId: String) {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        Toast.makeText(this, "emailPatternMatchButton Pattern.mathches() = ${pattern.matcher(userId).matches()}", Toast.LENGTH_SHORT).show()
    }

    private fun nicknamePatternMatchButton(nickname: String) {
        val regex = BuildConfig.NICK_NAME_REGEX
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(nickname)
        Toast.makeText(this, "nicknamePatternMatchButton Pattern.mathches() = ${matcher.matches()}", Toast.LENGTH_SHORT).show()
    }

    private fun carNoPatternMatchButton(carNo: String) {
        val ps = Pattern.compile(BuildConfig.CAR_NO_REGEX)
        Toast.makeText(this, "carNoPatternMatchButton Pattern.mathches() = ${ps.matcher(carNo).matches()}", Toast.LENGTH_SHORT).show()
    }
}
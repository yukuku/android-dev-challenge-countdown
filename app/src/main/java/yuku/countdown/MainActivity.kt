/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yuku.countdown

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import yuku.countdown.ui.theme.MyTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    var remain by remember { mutableStateOf(725L) }

    LaunchedEffect(0) {
        while (true) {
            remain -= 1L
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(elevation = 16.dp, shape = CircleShape) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 50)),
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xff990000))
                        .width(320.dp)
                        .height(320.dp)
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        JumpyClock(remain)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Button(onClick = { remain = (remain + 60L).coerceAtMost(3600L) }) {
                                Text("+1 min")
                            }
                            Box(Modifier.width(16.dp))
                            Button(onClick = { remain = (remain - 60L).coerceAtLeast(0L) }) {
                                Text("-1 min")
                            }
                        }
                        Box(Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Button(onClick = { remain = (remain + 10L).coerceAtMost(3600L) }) {
                                Text("+10 sec")
                            }
                            Box(Modifier.width(16.dp))
                            Button(onClick = { remain = (remain - 10L).coerceAtLeast(0L) }) {
                                Text("-10 sec")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JumpyClock(remain: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        val total = remain.coerceAtLeast(0L)
        val s = total % 60
        val m = (total / 60)

        JumpyDigit(m / 10)
        Box(Modifier.width(4.dp))
        JumpyDigit(m % 10)
        Box(Modifier.width(12.dp))
        Text(":", fontSize = 24.sp, color = Color.White, modifier = Modifier.offset(y = 92.dp))
        Box(Modifier.width(12.dp))
        JumpyDigit(s / 10)
        Box(Modifier.width(4.dp))
        JumpyDigit(s % 10)
    }
}

@Composable
fun JumpyDigit(digit: Long) {
    val offset by animateDpAsState(
        targetValue = digit.toInt().dp,
        animationSpec = keyframes {
            durationMillis = 400
            0.dp at 0
            10.dp at 15
            0.dp at 75
            (-40).dp at 225
            0.dp at 325
        },
    )

    val s = digit.toString()
    Box(
        modifier = Modifier
            .offset(y = 80.dp + offset - digit.toInt().dp)
            .clip(RoundedCornerShape(percent = 20)),
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color(0xffcccccc), Color(0xff999999))))
                .width(40.dp)
                .height(60.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(s, fontSize = 24.sp, color = Color.Black)
        }
    }
}

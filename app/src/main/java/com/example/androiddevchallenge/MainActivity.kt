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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import androidx.compose.runtime.*
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: MainActivityViewModel) {
    val counterStarted: Boolean by viewModel.counterStarted.observeAsState(initial = false)

    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Countdown with JetPack Compose",
                modifier = Modifier.offset(y = 24.dp)
            )
            TimerView(viewModel)
            Box(
                modifier = Modifier
                    .offset(y = (60).dp)
            ) {
                ClockView(animate = counterStarted)
            }
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .offset(y = (-40).dp)
                    .fillMaxHeight()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.toggleCounter() },
                    text = {
                        Text(
                            if (counterStarted) "Stop Countdown" else "Start Countdown",
                            color = Color.White
                        )
                    },
                    backgroundColor = Color.DarkGray
                )
            }

        }
    }
}

@Composable
fun ClockView(animate: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .size(150.dp)
            .border(
                width = 10.dp, if (animate) {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                } else Color.Gray, CircleShape
            )
            .rotate(if (animate) rotation else 0f)
            .clip(CircleShape)
            .background(if(isSystemInDarkTheme()) Color.Black else Color.White)
    ) {
        Box(
            modifier = Modifier.composed {
                align(Alignment.TopCenter)
                    .height(60.dp)
                    .width(8.dp)
                    .offset(y = (20).dp)
                    .clip(RectangleShape)
                    .background(
                        if (animate) {
                            if (isSystemInDarkTheme()) Color.White else Color.Black
                        } else Color.Gray
                    )
            }
        )
    }

}

@Composable
fun TimerView(viewModel: MainActivityViewModel) {
    val counter: Int by viewModel.counter.observeAsState(initial = 100)
    val counterStarted: Boolean by viewModel.counterStarted.observeAsState(initial = false)
    Row(
        modifier = Modifier.offset(y = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { viewModel.increaseInitialCounter() },
            enabled = !counterStarted,
            modifier = Modifier.offset(x = (-10).dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                tint = if (counterStarted) Color.Gray
                else {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                },
                contentDescription = "Increase time",
                modifier = Modifier.size(72.dp)
            )
        }
        Text(
            text = counter.toString(),
            fontSize = 100.sp
        )
        IconButton(
            onClick = { viewModel.decreaseInitialCounter() },
            enabled = !counterStarted,
            modifier = Modifier.offset(x = (10).dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease time",
                tint = if (counterStarted) Color.Gray
                else {
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                },
                modifier = Modifier.size(72.dp)
            )
        }

    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        //MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        //MyApp()
    }
}


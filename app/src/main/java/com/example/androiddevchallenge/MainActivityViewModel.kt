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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _counter = MutableLiveData<Int>(100)
    val counter: LiveData<Int> = _counter

    private val _counterStarted = MutableLiveData<Boolean>()
    val counterStarted: LiveData<Boolean> = _counterStarted

    var counterJob: Job? = null

    fun toggleCounter() {
        if (counterStarted.value == true) {
            _counterStarted.value = false
            counterJob?.cancel()
        } else {
            _counterStarted.value = true
            counterJob = viewModelScope.launch {
                while (_counter.value!! >= 0) {
                    delay(1000)
                    if (_counter.value == 0) {
                        counterJob?.cancel()
                        _counterStarted.value = false
                    } else {
                        _counter.value = _counter.value?.minus(1)
                    }
                }
            }
        }
    }

    fun increaseInitialCounter() {
        if (_counter.value!! < 999) {
            _counter.value = _counter.value?.plus(1)
        }
    }

    fun decreaseInitialCounter() {
        if (_counter.value!! >= 0) {
            _counter.value = _counter.value?.minus(1)
        }
    }
}

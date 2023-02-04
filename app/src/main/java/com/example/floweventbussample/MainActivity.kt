package com.example.floweventbussample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.checkdebugtoolsample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ds.helper.flowbus.FlowEventBus

class Event

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text: TextView = findViewById(R.id.text)

        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            Log.d("FlowEventBus", "publish event thread=${Thread.currentThread().name}")
            FlowEventBus.publish(Event())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                FlowEventBus.subscribe<Event> {
                    Log.d(
                        "FlowEventBus",
                        "received event on resume only thread=${Thread.currentThread().name}"
                    )
                    text.text = "${text.text}\nEvent Received(resumed only)"
                }
            }
        }

        lifecycleScope.launch {
            FlowEventBus.subscribe<Event> {
                Log.d("FlowEventBus", "received event thread=${Thread.currentThread().name}")
                text.text = "${text.text}\nEvent Received"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FlowEventBus", "onDestroy()")
    }
}
package com.kafka.common.widgets

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun Toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(LocalContext.current, message, duration).show()

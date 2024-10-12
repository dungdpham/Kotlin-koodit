package com.example.lab15

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab15.ui.theme.AndroidIntroTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (!hasPermissions())
                Toast.makeText(applicationContext, "No Audio Record Permission", Toast.LENGTH_LONG).show()
            else{
                AndroidIntroTheme {
                    RecordView()
                }
            }
        }
    }

    private fun hasPermissions(): Boolean {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DBG", "No audio recorder access")
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            return true
        }
        return true
    }
}

@Composable
fun RecordView(viewModel: AudioViewModel = viewModel()) {
    val context = LocalContext.current
    val recRunning by viewModel.recRunning.observeAsState(false)
    val recFiles by viewModel.recFiles.observeAsState(emptyList())
    val nowPlayingFile by viewModel.nowPlayingFile.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getFiles(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 8.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { if (recRunning) viewModel.stopRecord() else viewModel.startRecord(context) },
            colors = ButtonDefaults.buttonColors(containerColor = if (recRunning) Color.Red else Color.Green),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(256.dp)
                .height(96.dp)
                .padding(16.dp)

            ) {
            Text(
                text = if (recRunning) "STOP RECORD" else "START RECORD",
                fontSize = 24.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(recFiles) { file ->
                val nowPlaying = nowPlayingFile == file

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (nowPlaying) Color(0xFF0072BB) else Color.Transparent)
                        .clickable { viewModel.playAudio(file) }
                ) {
                    Text(
                        text = file.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

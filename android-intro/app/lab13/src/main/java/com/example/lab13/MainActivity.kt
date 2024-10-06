package com.example.lab13

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab13.ui.theme.AndroidIntroTheme

class MainActivity : ComponentActivity() {
    private lateinit var mBluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(applicationContext, "No Bluetooth LE capability", Toast.LENGTH_LONG)
                .show()
        } else {
            val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            mBluetoothAdapter = btManager.adapter

            if (!hasPermissions())
                Toast.makeText(applicationContext, "No Bluetooth permissions", Toast.LENGTH_LONG)
                    .show()
            else {
                setContent {
                    AndroidIntroTheme {
                        ShowDevices(
                            mBluetoothAdapter = mBluetoothAdapter
                        )
                    }
                }
            }
        }
    }

    private fun hasPermissions(): Boolean {
        if (!mBluetoothAdapter.isEnabled) {
            Log.d("DBG", "Bluetooth is not enable")
            return false
        } else if (
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("DBG", "No fine location access")
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_ADMIN
                ), 1
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ), 1
                )
            }
            return true
        }
        return true
    }
}

@Composable
fun ShowDevices(mBluetoothAdapter: BluetoothAdapter, model: MyViewModel = viewModel()) {
    val context = LocalContext.current
    val value: List<ScanResult>? by model.scanResults.observeAsState(null)
    val fScanning: Boolean by model.fScanning.observeAsState(false)

    val connectionState: Int by model.mConnectionState.observeAsState(0)
    val bpm: Int by model.mBPM.observeAsState(0)
    var selectedResult by rememberSaveable { mutableStateOf<ScanResult?>(null) }

    val scanner = mBluetoothAdapter.bluetoothLeScanner


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 8.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { model.scanDevices(scanner, context) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0072BB),
                disabledContainerColor = Color.Gray
            ),
            enabled = !fScanning,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(256.dp)
                .height(96.dp)
                .padding(16.dp)
        ) {
            Text(
                text = if (fScanning) "SCANNING" else "SCAN",
                fontSize = 24.sp,
                color = Color.White
            )
        }

        if (connectionState == MyViewModel.STATE_CONNECTED) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Connected " + if (bpm != 0) "$bpm bpm" else "",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        } else if (connectionState == MyViewModel.STATE_CONNECTING) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Connecting...",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(value ?: emptyList()) { result ->
                val isSelected = selectedResult == result

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = result.isConnectable) {
                            if (isSelected) {
                                model.disconnectDevice()
                                selectedResult = null
                            } else {
                                model.disconnectDevice()
                                model.connectDevice(result.device, context)
                                selectedResult = result
                            }
                        }
                        .background(if (isSelected) Color(0xFF0072BB) else Color.Transparent)

                ) {
                    Text(
                        text = "${result.device.address} ${result.scanRecord?.deviceName ?: "Unnamed device"} ${result.rssi}dBm",
                        color = if (result.isConnectable) Color.White else Color.Gray,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

//                Text(
//                    text = "${result.device.address} ${result.scanRecord?.deviceName ?: "Unnamed device"} ${result.rssi}dBm",
//                    color = if (result.isConnectable) Color.White else Color.Gray,
//                    fontSize = 20.sp,
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .clickable(enabled = result.isConnectable) {
//                            if (result.isConnectable) model.connectDevice(result.device, context)
//                        }
//                )
            }
        }
    }
}



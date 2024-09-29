package com.example.lab11

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class MyViewModel : ViewModel() {
    companion object GattAttributes {
        const val SCAN_PERIOD: Long = 3000

        const val STATE_DISCONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2

        val UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
        val UUID_HEART_RATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
        val UUID_CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    val scanResults = MutableLiveData<List<ScanResult>>(null)
    val fScanning = MutableLiveData<Boolean>(false)
    private val mResults = HashMap<String, ScanResult>()

    @SuppressLint("MissingPermission")
    fun scanDevices(scanner: BluetoothLeScanner, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            scanResults.postValue(emptyList())
            fScanning.postValue(true)

            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .build()
            scanner.startScan(null, settings, leScanCallback)
            Log.d("DBG", "Scan started!")

            delay(SCAN_PERIOD)

            scanner.stopScan(leScanCallback)
            fScanning.postValue(false)
            Log.d("DBG", "Scan stopped!")

            scanResults.postValue(mResults.values.toList())
        }
    }

    @SuppressLint("MissingPermission")
    fun startStopScan(scanner: BluetoothLeScanner, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (fScanning.value == true) {
                Log.d("DBG", "STOP SCAN")
                scanner.stopScan(leScanCallback)
                fScanning.postValue(false)
                scanResults.postValue(mResults.values.toList())
            } else {
                Log.d("DBG", "START SCAN")
                fScanning.postValue(true)
                val settings = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setReportDelay(0)
                    .build()
                scanner.startScan(null, settings, leScanCallback)

                delay(SCAN_PERIOD)

                Log.d("DBG", "STOP SCAN")
                scanner.stopScan(leScanCallback)
                fScanning.postValue(false)
                scanResults.postValue(mResults.values.toList())
            }
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val deviceAddress = device.address
            mResults[deviceAddress] = result

            Log.d("DBG", "Device address: $deviceAddress (${result.isConnectable})")
        }
    }
}
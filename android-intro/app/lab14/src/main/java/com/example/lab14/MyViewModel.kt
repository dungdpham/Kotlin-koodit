package com.example.lab14

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
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
        const val SCAN_PERIOD: Long = 5000

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

    private var mBluetoothGatt: BluetoothGatt? = null
    val mConnectionState = MutableLiveData<Int>(0)
    val mBPM = MutableLiveData<Int>(0)
    private val BPMlist = mutableListOf<Int>()
    val mBPMList = MutableLiveData<List<Int>>(null)

    @SuppressLint("MissingPermission")
    fun scanDevices(scanner: BluetoothLeScanner, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            mResults.clear()
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
    fun connectDevice(device: BluetoothDevice, context: Context) {
        Log.d("DBG", "Connecting to device @ ${device.address}")
        mConnectionState.postValue(STATE_CONNECTING)

        mBluetoothGatt = device.connectGatt(context, false, mGattCallback)
    }

    @SuppressLint("MissingPermission")
    fun disconnectDevice() {
        Log.d("DBG", "Disconnecting from existing GATT server")
        mBluetoothGatt?.disconnect()
    }


    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val deviceAddress = device.address
            mResults[deviceAddress] = result

            Log.d("DBG", "Device address: $deviceAddress (${result.isConnectable})")
            scanResults.postValue(mResults.values.toList())
        }
    }

    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.d("DBG", "GATT connection failure")
                return
            } else if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("DBG", "GATT connection success")
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("DBG", "Connected GATT service")
                mConnectionState.postValue(STATE_CONNECTED)

                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("DBG", "Disconnected GATT service")
                mConnectionState.postValue(STATE_DISCONNECTED)

                gatt.close()
                mBPM.postValue(0)
                BPMlist.clear()
                mBPMList.postValue(BPMlist.toList())
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Log.d("DBG", "onServicesDiscovered()")

            if (status != BluetoothGatt.GATT_SUCCESS) return

            for (gattService in gatt.services) {
                Log.d("DBG", "Service ${gattService.uuid}")

                if (gattService.uuid == UUID_HEART_RATE_SERVICE) {
                    Log.d("DBG", "Bingo!!! HEART_RATE_SERVICE found!")

                    for (gattCharacteristic in gattService.characteristics) {
                        Log.d("DBG", "Characteristic ${gattCharacteristic.uuid}")
                    }

                    val characteristic = gatt.getService(UUID_HEART_RATE_SERVICE).getCharacteristic(
                        UUID_HEART_RATE_MEASUREMENT)

                    gatt.setCharacteristicNotification(characteristic, true)

                    if (gatt.setCharacteristicNotification(characteristic, true)) {
                        val descriptor = characteristic.getDescriptor(
                            UUID_CLIENT_CHARACTERISTIC_CONFIG)
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)
                    }
                }
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.d("DBG", "onDescriptorWrite()")

            if (status == BluetoothGatt.GATT_SUCCESS)
                Log.d("DBG", "Notification enabled on the GATT server")
            else Log.d("DBG", "Failed to enable notification on the GATT server")
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            Log.d("DBG", "Characteristic data received")

            if (characteristic.uuid == UUID_HEART_RATE_MEASUREMENT) {
                val flags = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)
                val bpm: Int

                if ((flags and 0x01) != 0) {
                    bpm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1)
                    Log.d("DBG", "BPM (UINT16): $bpm")
                } else {
                    bpm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1)
                    Log.d("DBG", "BPM (UINT8): $bpm")
                }

                mBPM.postValue(bpm)
                BPMlist.add(bpm)
                mBPMList.postValue(BPMlist.toList())
            }
        }
    }
}



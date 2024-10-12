package com.example.lab15

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class AudioViewModel : ViewModel() {
    val recRunning = MutableLiveData<Boolean>(false)
    val recFiles = MutableLiveData<List<File>>()
    val nowPlayingFile = MutableLiveData<File?>()
    private var isPlaying = false

    private var track: AudioTrack? = null
    private var recorder: AudioRecord? = null

    fun getFiles(context: Context) {
        val storageDir =context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        recFiles.postValue(storageDir?.listFiles()?.filter { it.extension == "raw" } ?: emptyList())
    }

    fun playAudio(file: File) {
        viewModelScope.launch(Dispatchers.Main) { nowPlayingFile.postValue(file) }
//        nowPlayingFile.postValue(file)

        viewModelScope.launch(Dispatchers.IO) {
            stopAudio().join()

            val minBufferSize = AudioTrack.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val aBuilder = AudioTrack.Builder()
            val aAttr: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            val aFormat: AudioFormat = AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build()
            track = aBuilder
                .setAudioAttributes(aAttr)
                .setAudioFormat(aFormat)
                .setBufferSizeInBytes(minBufferSize)
                .build()
//            track?.setVolume(0.2f)

            track?.play()
            isPlaying = true

            val istream = FileInputStream(file)
            val buffer = ByteArray(minBufferSize)
            var i = 0

            try {
                i = istream.read(buffer, 0, minBufferSize)
                while (isPlaying && (i != -1)) {
                    track?.write(buffer, 0, i)
                    i = istream.read(buffer, 0, minBufferSize)
                }
            } catch (e: IOException) {
                Log.e("DBG", "Error playing file: $e")
            } finally {
                try {
                    istream.close()
                } catch (e: IOException) {
                    Log.e("DBG", "Error closing stream: $e")
                }
                stopAudio().join()
                if (!isPlaying) nowPlayingFile.postValue(null)
            }
        }
    }

    private fun stopAudio(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            if (isPlaying) {
                isPlaying = false
                try {
                    track?.stop()
                } catch (e: Exception) {
                    Log.e("DBG", "Error stopping audio: $e")
                } finally {
                    track?.release()
                    track = null
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun startRecord(context: Context) {
        if (recRunning.value == true) return

        viewModelScope.launch(Dispatchers.IO) {
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            var recFile: File? = null

            try {
                recFile = File(storageDir, "record_audio_${System.currentTimeMillis()}.raw")
            } catch (e: IOException) {
                Log.e("DBG", "Error creating file: $e")
            }
//            val recFile = File(storageDir, "record_audio_${System.currentTimeMillis()}.raw")

            recRunning.postValue(true)

            try {
                val outputStream = FileOutputStream(recFile)
                val bufferedOutputStream = BufferedOutputStream(outputStream)
                val dataOutputStream = DataOutputStream(bufferedOutputStream)

                val minBufferedSize = AudioRecord.getMinBufferSize(
                    44100,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val aFormat = AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(44100)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .build()
                recorder = AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(aFormat)
                    .setBufferSizeInBytes(minBufferedSize)
                    .build()

                recorder?.startRecording()

                val audioData = ByteArray(minBufferedSize)
                while (recRunning.value == true) {
                    val numOfBytes = recorder?.read(audioData, 0, minBufferedSize) ?: 0
                    if (numOfBytes > 0) {
                        dataOutputStream.write(audioData)
                    }
                }

                recorder?.stop()
                dataOutputStream.close()
                outputStream.close()

                getFiles(context)
            } catch (e: IOException) {
                Log.e("DBG", "Error recording: $e")
            } finally {
                recorder?.release()
                recorder = null
            }
        }
    }

    fun stopRecord() {
        recRunning.postValue(false)
    }
}
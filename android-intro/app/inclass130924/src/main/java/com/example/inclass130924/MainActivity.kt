package com.example.inclass130924

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.inclass130924.ui.theme.AndroidIntroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidIntroTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

val game = GuessGame(1..10)
@Composable
fun GameView(modifier: Modifier = Modifier) {
    var guess by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column {
        Text(
            text = "${game.range.first}..${game.range.last}",
            modifier = modifier
        )
        TextField(
            value = guess,
            onValueChange = { guess = it },
            label = { Text("Your guess: ") }
        )
        Text("Result: $result")
        Button(onClick = { result = game.makeGuess(guess.toInt()).toString() }) {
            Text("Try")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidIntroTheme {
        GameView()
    }
}
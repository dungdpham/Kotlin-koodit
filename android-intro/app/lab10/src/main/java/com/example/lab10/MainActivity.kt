package com.example.lab10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab10.ui.theme.AndroidIntroTheme

enum class GuessResult {
    HIGH, LOW, HIT
}

class NumberGame(val range: IntRange) {
    private val secret = range.random()
    var guesses = listOf<Int>()
        private set

    fun makeGuess(guess: Int): GuessResult {
        guesses = guesses.plus(guess)

        return when (guess) {
            in Int.MIN_VALUE..<secret -> GuessResult.LOW
            in secret+1..Int.MAX_VALUE -> GuessResult.HIGH
            else -> GuessResult.HIT
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidIntroTheme {
                val game = NumberGame(1..10)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameView(
                        game = game,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameView(game: NumberGame, modifier: Modifier = Modifier) {
    var guess by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy((8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Hello! Guess a number in ${game.range.first}..${game.range.last}")

        OutlinedTextField(
            value = guess,
            onValueChange = {
                guess = it
                result = ""
                isError = false
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            label = { Text("Your guess") },
            isError = isError
        )

        Button(onClick = {
            guess.toIntOrNull()?.takeIf { it in game.range }?.let {
                result = game.makeGuess(it).toString()
            } ?: run { isError = true }
        }) { Text("Make guess!") }

        if (result.isNotEmpty()) Text("$result ${game.guesses.sorted()}")
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GamePreview() {
    AndroidIntroTheme {
        GameView(NumberGame(1..10))
    }
}
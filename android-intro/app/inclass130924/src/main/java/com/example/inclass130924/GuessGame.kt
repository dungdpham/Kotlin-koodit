package com.example.inclass130924

import kotlin.random.Random

class GuessGame(val range: IntRange) {
    private val secret = range.random()
    private val guesses: MutableList<Int> = mutableListOf()

    fun makeGuess(guess: Int): GameResults {
        guesses.add(guess)
        return when {
            (guess < secret) -> GameResults.LOW
            (guess > secret) -> GameResults.HIGH
            else -> GameResults.HIT
        }
    }
}
package com.example.lab10

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
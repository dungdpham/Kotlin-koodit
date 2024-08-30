import java.util.*

class Lotto(private val lottoRange: IntRange, private val n: Int) {
    private var secretNumbers: List<Int> = listOf()

    init {
        require(lottoRange.all { it in 0..Int.MAX_VALUE }) {"Lotto range must be between 0 and ${Int.MAX_VALUE}"}
        require(n > 0) { "Amount of secret numbers must be a positive integer" }
    }

    fun pickNDistinct(range: IntRange, n: Int): List<Int> {
        return range.toList().shuffled().take(n).sorted()
    }

    fun numDistinct(list: List<Int>): Int {
//        return list.distinct().size
        return list.toSet().size
    }

    fun numCommon(list1: List<Int>, list2: List<Int>): Int {
//        return list1.toSet().intersect(list2.toSet()).size
//        return list1.filter { it in list2 }.toSet().size
        return list1.toSet().count { it in list2 }
    }

    fun isLegalLottoGuess(guess: List<Int>, range: IntRange = lottoRange, count: Int = n): Boolean {
        return guess.size == count && numDistinct(guess) == count && guess.all { it in range}
    }

    fun checkGuess(guess: List<Int>, secret: List<Int> = secretNumbers): Int {
//        return if (isLegalLottoGuess(guess)) guess.count { it in secret } else 0
        return if (isLegalLottoGuess(guess)) numCommon(guess, secret) else 0
    }

    fun readNDistinct(low: Int, high: Int, n: Int): List<Int> {
        var guess: List<Int>
        do {
            print("Give $n numbers from $low to $high, separated by commas: ")
            guess = readln().filterNot { it.isWhitespace() }.split(",").mapNotNull { it.toIntOrNull() }.sorted()
        } while (guess.toSet().size != n || guess.size != n || !guess.all { it in low..high })
        return guess
    }

    fun playLotto() {
        var choice: String
        var guess: List<Int>
        do {
            secretNumbers = pickNDistinct(lottoRange, n)
            guess = readNDistinct(lottoRange.first, lottoRange.last, n)
            println("Lotto numbers: $guess, you got ${checkGuess(guess)} correct!")
            println("Secret numbers: $secretNumbers")
            do {
                print("More? (Y/N): ")
                choice = readln().uppercase(Locale.getDefault())
            } while (choice != "Y" && choice != "N")
        } while (choice == "Y")
    }
}

fun main() {
    val l = Lotto(1..40, 7)
//    val guess = l.pickNDistinct(1..20, 7)
//    println("Guess: $guess")
//    println(l.numDistinct(listOf(1, 2, 3, 3, 3, 4, 5, 5)))
//    println(l.numCommon(listOf(1, 1, 2, 3, 3, 3, 4), listOf(3, 3, 4, 5, 5)))
//    println(l.isLegalLottoGuess(guess = listOf(23, 9, 4, 23, 33, 34, 15)))
//    println(l.isLegalLottoGuess(guess))
//    println("${l.checkGuess(guess)} correct number(s)")
//    l.readNDistinct(0, 40, 7)
    l.playLotto()
}
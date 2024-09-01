import java.util.*
import kotlin.math.min

class Lotto(private val lottoRange: IntRange, private val n: Int) {
    private var secretNumbers: List<Int> = listOf()

    init {
        require(lottoRange.all { it in 0..Int.MAX_VALUE }) {"Lotto range must be between 0 and ${Int.MAX_VALUE}"}
        require(n > 0) { "Amount of secret numbers must be a positive integer" }
        require(lottoRange.toList().size >= n) { "Lotto range must have at least $n integers" }
    }

    fun pickNDistinct(range: IntRange, n: Int): List<Int>? {
        return if (range.toList().size < n) null else range.toList().shuffled().take(n).sorted()
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
            secretNumbers = requireNotNull(pickNDistinct(lottoRange, n)) {"Not enough integers in range"}

            guess = readNDistinct(lottoRange.first, lottoRange.last, n)
            println("Lotto numbers: $guess, you got ${checkGuess(guess)} correct!")
//            println("Secret numbers: $secretNumbers")

            val (steps, results) = findLotto(this)
            println("Computer guess in $steps steps is $results")
            do {
                print("More? (Y/N): ")
                choice = readln().uppercase(Locale.getDefault())
            } while (choice != "Y" && choice != "N")
        } while (choice == "Y")
    }

    private fun findLotto(lotto: Lotto): Pair<Int, List<Int>> {
        var steps = 0

        var corrects: MutableSet<Int> = mutableSetOf()
        var incorrects: MutableSet<Int> = mutableSetOf()
        var unsure: MutableSet<Int> = mutableSetOf()

        val firstGuess: List<Int> = lottoRange.take(lotto.n)
        val checkList: List<Int> = lottoRange.filterNot { it in firstGuess }

        val x = lotto.checkGuess(firstGuess)
        steps += 1
//        println("$steps. guess: Check $firstGuess: $x corrects.")

        if (x == lotto.n) corrects = firstGuess.toMutableSet()

        var i = 0
        var j = 0
        var y: Int
        var guess: List<Int>

        while (corrects.size != lotto.n) {
            guess = firstGuess - firstGuess[i] + checkList[j]
            y = lotto.checkGuess(guess)
            steps += 1
//            print("$steps. guess: Check $guess: $y corrects. ")

            if (y == lotto.n) {
                corrects = guess.toMutableSet()
//                println()
                break
            }

            if (y == x) {
                if (checkList[j] !in incorrects && checkList[j] !in corrects) unsure.add(checkList[j])
                else {
                    if (checkList[j] in incorrects) incorrects.add(firstGuess[i])
                    if (checkList[j] in corrects) corrects.add(firstGuess[i])

                    i = min(i + 1, firstGuess.size - 1)
                }
            } else if (y < x) {
                incorrects.add(checkList[j])
                corrects += unsure + firstGuess[i]
                unsure.clear()

                i = min(i + 1, firstGuess.size - 1)
            } else {
                corrects.add(checkList[j])
                incorrects += unsure + firstGuess[i]
                unsure.clear()

                i = min(i + 1, firstGuess.size - 1)
            }

            if ((corrects - firstGuess.toSet()).size == (firstGuess.size - x))
                incorrects += checkList - (corrects - firstGuess.toSet())
            else if ((incorrects - firstGuess.toSet()).size == (checkList.size - (lotto.n - x)))
                corrects += checkList - (incorrects - firstGuess.toSet())

            j = min(j + 1, checkList.size - 1)

//            println("-> $corrects. Incorrects found: ${incorrects.size}. Unsure: ${unsure.size}")

            if (incorrects.size == lotto.lottoRange.toList().size - lotto.n)
                corrects = (lotto.lottoRange.toSet() - incorrects).toMutableSet()
        }

        return Pair(steps, corrects.toList().sorted())
    }

//    fun testFindLotto() {
//        var secretTest: List<Int>
//        while (true) {
//            do {
//                print("Secrets: ")
//                secretTest = readln().filterNot { it.isWhitespace() }.split(",").mapNotNull { it.toIntOrNull() }.sorted()
//            } while (!isLegalLottoGuess(secretTest))
//
//            secretNumbers = secretTest
//            var (steps, results) = findLotto(this)
//            println("Computer guess in $steps steps is $results.\n")
//        }
//    }
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
//    l.testFindLotto()
    l.playLotto()
}
import kotlin.math.abs

class FractionMutable(numerator: Int, denominator: Int, sign: Int = 1) {
    var numerator: Int = sign * numerator
        private set
    var denominator: Int = denominator
        private set

    init {
        require(numerator > 0 && denominator > 0 ) {"Numerator and Denominator must be integers greater than 0"}
        require(sign == 1 || sign == -1) {"Sign can only take value 1 or -1"}

        simplifyThis()
    }

    private fun simplifyThis() {
        val gcd = getGcd(abs(numerator), abs(denominator))
        numerator = if (numerator * denominator < 0)
            -abs(numerator) / gcd
        else abs(numerator) / gcd
        denominator = abs(denominator) / gcd
    }

    override fun toString(): String {
        return "$numerator/$denominator"
    }

    private fun getGcd(a: Int, b: Int): Int {
        return if (b == 0) a else getGcd(b, a % b)
    }

    fun negate() {
        numerator = -numerator
    }

//    fun add(other: FractionMutable) {
//        numerator = numerator * other.denominator + denominator * other.numerator
//        denominator = denominator * other.denominator
//        simplifyThis()
//    }
//
//    fun mult(fraction: FractionMutable) {
//        numerator *= fraction.numerator
//        denominator *= fraction.denominator
//        simplifyThis()
//    }
//
//    fun div(fractionMutable: FractionMutable) {
//        numerator *= fractionMutable.denominator
//        denominator *= fractionMutable.numerator
//        simplifyThis()
//    }

    private fun calculate(arithmetic: (a: Int, b: Int, c: Int, d: Int) -> Pair<Int, Int> ):
                (FractionMutable) -> Unit = { other: FractionMutable ->
        val (newNum, newDen) = arithmetic(numerator, denominator, other.numerator, other.denominator)
        numerator = newNum
        denominator = newDen
        simplifyThis()
    }

//    private val addFractions = { a: Int, b: Int, c: Int, d: Int -> Pair(a*d + b*c, b*d)}
//    private val multFractions = { a: Int, b: Int, c: Int, d: Int -> Pair(a*c, b*d)}
//    private val divFractions = { a: Int, b: Int, c: Int, d: Int -> Pair(a*d, b*c)}
//
//    val add = calculate(addFractions)
//    val mult = calculate(multFractions)
//    val div = calculate(divFractions)

    val add = calculate { a: Int, b: Int, c: Int, d: Int -> Pair(a*d + b*c, b*d)}
    val mult = calculate { a: Int, b: Int, c: Int, d: Int -> Pair(a*c, b*d)}
    val div = calculate { a: Int, b: Int, c: Int, d: Int -> Pair(a*d, b*c)}

    fun intPart(): Int {
        return numerator / denominator
    }
}

fun main() {
    val a = FractionMutable(1,2,-1)
    a.add(FractionMutable(1,3))
    println(a)
    a.mult(FractionMutable(5,2, -1))
    println(a)
    a.div(FractionMutable(2,1))
    println(a)
}
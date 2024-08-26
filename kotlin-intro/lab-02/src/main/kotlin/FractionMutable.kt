import kotlin.math.abs

class FractionMutable(numerator: Int, denominator: Int, sign: Int = 1) {
    var numerator: Int = 0
        private set
    var denominator: Int = 1
        private set

    init {
        require(numerator > 0 && denominator > 0 ) {"Numerator and Denominator must be integers greater than 0"}
        require(sign == 1 || sign == -1) {"Sign can only take value 1 or -1"}

        val gcd = getGcd(numerator, denominator)
        this.numerator = sign * (numerator / gcd)
        this.denominator = denominator / gcd
    }

    private fun getGcd(a: Int, b: Int): Int {
        return if (b == 0) a else getGcd(b, a % b)
    }

    override fun toString(): String {
        return "$numerator/$denominator"
    }

    fun negate() {
        numerator = -numerator
    }

    fun add(fraction: FractionMutable) {
        val a = numerator * fraction.denominator + denominator * fraction.numerator
        val b = denominator * fraction.denominator
        val gcd = getGcd(abs(a), abs(b))
        numerator = a / gcd
        denominator = b / gcd
    }

    fun mult(fraction: FractionMutable) {
        val a = numerator * fraction.numerator
        val b = denominator * fraction.denominator
        val gcd = getGcd(abs(a), abs(b))
        numerator = a / gcd
        denominator = b / gcd
    }

    fun div(fractionMutable: FractionMutable) {
        val a = numerator * fractionMutable.denominator
        val b = denominator * fractionMutable.numerator
        val gcd = getGcd(abs(a), abs(b))
        numerator = a / gcd
        denominator = b / gcd
    }

    fun intPart(): Int {
        return numerator / denominator
    }
}
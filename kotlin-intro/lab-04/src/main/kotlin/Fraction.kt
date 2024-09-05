import java.util.*
import kotlin.math.abs

class Fraction(numerator: Int, denominator: Int, private val sign: Int = 1) : Comparable<Fraction> {
    init {
        require(numerator >= 0 && denominator > 0)
        require(sign == 1 || sign == -1)
    }
    private val num = abs(numerator / getGcd(numerator, denominator))
    private val denom = abs(denominator / getGcd(numerator, denominator))

    private fun getGcd(a: Int, b: Int): Int {
        return  if (b == 0) a else getGcd(b, a % b)
    }

    override fun toString(): String = "${if (sign == -1 ) "-" else ""}$num/$denom"

    override fun equals(other: Any?): Boolean = (this === other) ||
            (other is Fraction
                    && this.num == other.num
                    && this.denom == other.denom
                    && this.sign == other.sign)

//    override fun hashCode(): Int {
//        var result = sign
//        result = 31 * result + num
//        result = 31 * result + denom
//        return result
//    } //Auto suggest by IntelliJ

    override fun hashCode(): Int = Objects.hash(num, denom, sign)

    override fun compareTo(other: Fraction): Int = when {
        this.sign * this.num * other.denom > other.sign * other.num * this.denom -> 1
        this.sign * this.num * other.denom < other.sign * other.num * this.denom -> -1
        else -> 0
    }

    fun negate(): Fraction = Fraction(num, denom, -sign)

    operator fun unaryPlus(): Fraction = this

    operator fun unaryMinus(): Fraction = this.negate()

    fun add(other: Fraction): Fraction {
        val numerator = this.sign * this.num * other.denom + other.sign * other.num * this.denom
        val denominator = this.denom * other.denom
        val sign = if (numerator < 0) -1 else 1
        return Fraction(abs(numerator), denominator, sign)
    }

    operator fun plus(other: Fraction): Fraction = this.add(other)

    fun subtr(other: Fraction): Fraction = this.add(-other)

    operator fun minus(other: Fraction): Fraction = this.subtr(other)

    fun mult(other: Fraction): Fraction {
        val numerator = this.num * other.num
        val denominator = this.denom * other.denom
        val sign = this.sign * other.sign
        return Fraction(numerator, denominator, sign)
    }

    operator fun times(other: Fraction): Fraction = this.mult(other)

    fun divide(other: Fraction): Fraction? =
        if (other.num != 0) this.mult(Fraction(other.denom, other.num, other.sign))
        else null

    operator fun div(other: Fraction): Fraction? = this.divide(other)
}

fun main() {
    val a = Fraction(1,2,-1)
    println(a)
    println(a.add(Fraction(1,3)))
    println(a.mult(Fraction(5,2, -1)))
    println(a.div(Fraction(2,1)))
    println(-Fraction(1,6) + Fraction(1,2))
    println(Fraction(2,3) * Fraction(3,2))
    println(Fraction(1,2) > Fraction(2,3))

    println(Fraction(11,12) < Fraction(17,18))
    val b = mutableSetOf(Fraction(1,3 ), Fraction(1,2,-1))
    b.add(Fraction(6,1))
    b.add(-Fraction(32,64))
    println(b.sorted())
}

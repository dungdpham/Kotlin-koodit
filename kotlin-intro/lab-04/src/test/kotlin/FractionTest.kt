import org.junit.jupiter.api.Test

internal class FractionTest {

    @Test
    fun consBasic1() {
        val a = Fraction(1,2)
        assert(a.toString() == "1/2")
    }

    @Test
    fun consBasic2() {
        val a = Fraction(6,12)
        assert(a.toString() == "1/2")
    }

    @Test
    fun consNeg() {
        val a = Fraction(1,2, -1)
        assert(a.toString() == "-1/2")
    }

    @Test
    fun consReduce() {
        val a = Fraction(4,20)
        assert(a.toString() == "1/5")
    }

    @Test
    fun consReduceNeg() {
        val a = Fraction(4,20, -1)
        assert(a.toString() == "-1/5")
    }

    @Test
    fun compareToEq() {
        assert(Fraction(1,2).compareTo(Fraction(2,4)) == 0)
    }

    @Test
    fun compareToGt() {
        assert(Fraction(1,2).compareTo(Fraction(2,5)) == 1)
    }

    @Test
    fun compareToLt() {
        assert(Fraction(1,3).compareTo(Fraction(2,5)) == -1)
    }

    @Test
    fun negate1() {
        assert(Fraction(1,2).negate().toString() == "-1/2")
    }

    @Test
    fun negate2() {
        assert(Fraction(1,2, -1).negate().toString() == "1/2")
    }

    @Test
    fun add() {
        assert(Fraction(11,12).add(Fraction(1,6)) == Fraction(13,12))
    }

    @Test
    fun plus() {
        assert(Fraction(2,3) + Fraction(1,3) == Fraction(1,1))
    }

    @Test
    fun subtr() {
        assert(Fraction(1,2).subtr(Fraction(1,3)) == Fraction(1,6))
    }

    @Test
    fun minus() {
        assert(Fraction(5,6) - Fraction(1,3) == Fraction(1,2))
    }

    @Test
    fun mult() {
        assert(Fraction(13,21).mult(-Fraction(3,5)) == Fraction(13,35,-1))
    }

    @Test
    fun times() {
        assert(Fraction(3,6) * Fraction (2, 6) == Fraction(1,6))
    }

    @Test
    fun div() {
        assert(Fraction(7,4) / -Fraction(2,3) == Fraction(21,8, -1))
    }

    @Test
    fun div2() {
        assert(Fraction(2,3,-1).divide(Fraction(5,6)) == Fraction(4,5,-1)   )
    }
}
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

// functions are introduced with keyword fun, function name, parameter list, and return type
// see https://kotlinlang.org/docs/functions.html

fun dist1(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val dist = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)
    return sqrt(dist)
}
dist1(1.0,0.0,5.0,3.0)
dist1(x1 = 1.0,y1 = 0.0,x2 = 5.0,y2 = 3.0)

fun isPalindrome1(s: String): Boolean {
    return s.reversed() == s
}
isPalindrome1("abba")
fun isPalindrome2(s: String) = s.reversed() == s
isPalindrome2("kukkuu")

// extension mechanism, more in next week material but here because it is so handy
fun String.isPalindrome(): Boolean {
    return this == this.reversed()
}
"abbab".isPalindrome()

fun pow1(b: Int, e: Int): Int? {
    //println("In function pow1(): b has velue $b, e has value $e")
    if (e < 0) {
        return null
    } else {
        var r = 1
        for (i in 1..e) {
            r *= b
        }
        return r
    }
}

pow1(b = 2,e = 10)
pow1(4,2)
pow1(10, 0)
pow1(0, 222)
pow1(4, -2)

// parameters may have default values:
// see https://kotlinlang.org/docs/functions.html#default-arguments
fun pow2(b: Int = 2, e: Int = 10): Int? {
    //println("In function pow2(): b has velue $b, e has value $e")
    return pow1(b, e)
}

pow2()
pow2(e = 8) // may, and in this case a must, name the parameter

// function with no return value - use type Unit, or omit return type
// see https://kotlinlang.org/docs/functions.html#unit-returning-functions
fun printpows1(base: Int, toexp: Int): Unit {
    for (i in 0..abs(toexp)) {
        println("$base to $i = ${pow1(base, i)}")
    }
}

printpows1(2,10)

// write recursive pow as a single-expression function
// see https://kotlinlang.org/docs/functions.html#single-expression-functions
fun pow3(b: Int = 2, e: Int): Int = if (e <= 0) 1 else b*pow3(b, e-1)

pow3(2,10)

// For non-recursive definitions return type may be omitted
fun pow4(b: Int, e: Int) = if(e < 0) 1.0 / pow3(b, -e).toDouble() else pow3(b, e).toDouble()

pow4(2, -3)

// random number from a range
fun pickNumber(range: IntRange = 1..6): Int {
    return range.random()
}

fun pickNumber1(range: IntRange = 1..6) = range.random()
pickNumber1()

// pick n numbers from range 1..6 with pickNumber() and return mean (= expectation)
fun pickNmean(n: Int): Double? {
    var sum = 0
    for (i in 1..n) {
        sum += pickNumber()
    }
    return if (n == 0) null else sum.toDouble() / n
}

pickNmean(500000)

// higher-order functions - function as a parameter or return type of a function
// function as a parameter is more common use case

// some commonly used higher-order functions: map and filter

// map - two parameters: a collection, and a function that accepts collection item type parameter
// return a new collection where members are the results of applying the function to all original collection items
fun sqr(x: Int) = x * x

listOf(2, 8, 4, 3).map { sqr(it) } // 'it' refers to each single item in the collection from beginning to end

// shorter way:
listOf(3, 5, 2, 1).map { it*it }

// more:
listOf("In", "Xanadu", "did", "Kublai", "Khan").map { it.length }
listOf("In", "Xanadu", "did", "Kublai", "Khan").map { it.length }.sum()
listOf("In", "Xanadu", "did", "Kublai", "Khan").map { it.lowercase().reversed().replaceFirstChar { it.uppercase() } }
listOf("In", "Xanadu", "did", "Kublai", "Khan").map { it.length }.map { if (it < 6) 1 else 0 }.sum()

listOf("In", "Xanadu", "did", "Kublai", "Khan").map { it.lowercase().reversed() }

// filter - two parameters: a collection, and a boolean-valued function
// return those items in the collection for which the function returns true

fun smallerThan6(i: Int) = i < 6

listOf(1,3,6,7).filter { smallerThan6(it) }

// and 'it' is available like with map
listOf(1,3,6,7).filter { it < 6 }

// how many times does number i appear in list numbers?
fun numOccurences1(i: Int, numbers: List<Int>) = numbers.filter { it == i }.size
// write numOcuurences with map?
fun numOccurences2(i: Int, numbers: List<Int>) =
    numbers
        .map { if (it == i) 1 else 0 }
        .sum()

numOccurences2(6, listOf(5,6,6,5,3,4,7,6))

val zzzzz = listOf(1,1,2,3,4,4,4,4,5,4,4,5,4).map { if (it == 4) 1 else 0 }
// 7

// writing higher-order functions
// function with a function parameter
fun transform10(func: (Int) -> Int): List<Int> {
    val list = (0..10).toList()
    var result = mutableListOf<Int>()
    for (e in list) {
        result.add(func(e))
    }
    return result
}

// how to define a function that can be used as parameter
// unnamed, or lambda, function. Expression in curly braces, on left-hand side
// of arrow -> list of parameters, on right-hand side the result expression
transform10({ x: Int -> x * x + 100 })

// if you have only one function parameter, you can omit parenthesis
transform10 { x: Int -> x * x }

// we can use standard function also
fun cubic(x: Int) = x * x * x;
transform10({cubic(it)})

// a lambda expression can be assigned to a variable
val cub = { x: Int -> x * x * x }
transform10(cub)

// define a function that returns the distance of a 2d point from origo
// here we allow for using different ways of computing distance (the function metric)
fun distanceFromOrigo(x: Int, y: Int, metric: (x1: Int, y1: Int, x2: Int, y2: Int) -> Double): Double {
    return metric(0, 0, x, y)
}

// normal square root of sum of squares distance (Pythagoras)
fun sqrdist(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return sqrt(((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)).toDouble())
}

// define L2 metric (same as above) with help of sqrdist function
val l2metric = { x1: Int, y1: Int, x2: Int, y2: Int -> sqrdist(x1,y1,x2,y2) }

// another way of computing the distance, called manhattan distance -
// think about driving in a city where all blocks are perfectly lined rectangles
val l1metric = { x1: Int, y1: Int, x2: Int, y2: Int -> (abs(x1-x2) + abs(y1-y2)).toDouble() }

distanceFromOrigo(3,4, l2metric)

// let's modify distanceFromOrigo so that it returns a function that computes distance

fun distFromOrigo(metric: (x1: Int, y1: Int, x2: Int, y2: Int) -> Double): (x: Int, y: Int) -> Double {
    return { x:Int, y:Int -> metric(0, 0, x, y) }
}

val dfo1 = distFromOrigo(l1metric)
dfo1(3,4)
val dfo2 = distFromOrigo(l2metric)
dfo2(3,4)
val dfoinf = distFromOrigo({x1: Int, y1:Int, x2: Int, y2: Int -> max(abs(x1-x2),abs(y1-y2)).toDouble()})
dfoinf(3, 4)

// When assigning a function to a variable, care must be taken that
// the function code is inside the square brackets.
// Otherwise, the code is run in the context of the placement, and
// the value of the variable becomes the result of the expression, i.e.
// In the example below, println("Not ok")

// println() does not return a value -> type is Unit
// If the pressure is not < 75, then the value of status1 will be println("Not ok")
// NOTE: Kotlin worksheet prints Not ok at the top of the panel.
var boilerPressure = 80
val status1 = if (boilerPressure < 75) println("Ok") else println("Not ok")
boilerPressure = 20 // not ok printed, even if boilerPressure is 20
status1

// println() does not return a value -> type is () -> Unit
boilerPressure = 80
val status2 = { if (boilerPressure < 75) println("Ok") else println("Not ok") }
boilerPressure = 20
status2()
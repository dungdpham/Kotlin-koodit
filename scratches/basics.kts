// 1 - elementary basics

5+8
4*3
1+2

// basic types can act as objects - plus(), minus() etc. methods available
5.plus(8)
8 % 3
8.rem(3)

5+6-2*4
5.plus(6.minus(2.times(4)))

// floats remain floats in arithmetic ops
3.5 + 3.5
3.5.times(2)
3.5 * 2

// variable declaration: "val" - value of variable can't be changed
// after first assignment (immutable)
val a: Int = 6 // final int a = 6 // Java?

// type conversions are explicit - no implicit conversions
val b: Int = 6.0.toInt()

// inferred typing - type for variable is inferred from the
// type of value assigned to the variable
val c = 6.0 // val c: Double = 6.0
val d = 3 / 2       // integer division
d

// pay attention to floating point number accuracy
val e = 0.8 - 0.2
e

// comparing floats for equality can give surprising results
if(0.8 - 0.2 == 0.6) "all fine" else "world is broken"

// variable declaration: "var" - value of variable may be changed
// after first assignment (mutable)
var x = 5
x
x = 5 * x
x
x == 26


// Strings behave quite like expected:
val s1: String = "In Xanadu"
val s2 = "did"
s1 + " " + s2
val s2ondid = s2 == "Did"     // This does not work in Java, in Kotlin comparision calls equals()
s2ondid

// string templates are often handy (like in many scripting languages)
val s3 = "$s1 $s2 Kublai Khan"
s3

// expressions in string templates can be used inside { and }
"$x squared is ${x*x}"

// Strings are objects; method call syntax works here, too
s1.reversed()
"a stately pleasure dome".reversed()

// 2 - some statements in Java are expressions in Kotlin

// if-statement returns a value
// https://kotlinlang.org/docs/control-flow.html#if-expression
val xx = 25
val w = if(xx < 25) "small" else "large"
w

// the type of the value returned from if statement is the closest
// superclass of the types of values returned in branches
val u = if(xx < 25) "small" else 100

// it is ok not to use the return value, so if can be used in traditional way
if(x <= 25) {
    println("small")
} else {
    println("large")
}

// no surprises in comparison operators and logical connectives
// <, >, <=, >=, ==, !=, && etc.

// range can be useful in comparisons
val rng = 1..10
if(x in rng) x+1 else x-1
// or (because it is always legal to replace
// val variable reference with its value):
if(x in 1..10) x+1 else x-1

// if with multiple branches is available
if(x < 0) {
    println("<0")
} else if(x == 0) {
    println("== 0")
} else {
    println(">0")
}

// when is sometimes useful (it is like enhanced switch in Java)
// Note that when is an expression, it returns a value
// see https://kotlinlang.org/docs/control-flow.html#when-expression
val y = when(x) {
    in Int.MIN_VALUE..-1 -> "< 0"
    0 -> "== 0"
    else -> "> 0"
}
y

// 3 - nullability

// variables normally can't be assigned null
// see https://kotlinlang.org/docs/null-safety.html#nullable-types-and-non-null-types
val sum: Int = 10
val count: Int = 0

// when "?" is appended to type name, the variable becomes nullable,
// ie., value "null" can be assigned to the variable
val mean: Double? = if (count == 0) null else sum.toDouble() / count
mean

// type inference works here, too (but is it clear programming?):
val mean1 = if (count == 0) null else sum.toDouble() / count
mean1

// if a variable is nullable, i.e., it may have value null, it is not
// possible to call methods without first checking against null:
val totalmean1 = if (mean1 != null) mean1+10.0 else 0.0

// elvis operator ("?:") returns the variable value (if it is not null)
// or the value given after the operator
val totalmean2 = mean1 ?: 0.666
totalmean2

// "?." between variable name and method:
// call the method only if variable is not null
val totalmean3 = mean?.plus(10.0)
totalmean3

// combine "?." and "?:"
mean1?.times(4)
val totalmean4 = mean?.plus(10.0) ?: 0.0

// !! forces method call - no null checking is done.
// This is not a good practice. Any exercise solutions where !! appears
// will be worth 0 points.
// val totalmean5 = mean1!! + 10.0

// some functions return nullable values:
val valofstr = "21a".toIntOrNull()
valofstr
// if you do "2g4".toInt() an exception will be thrown
// "21a".toInt()

// 4 - List and Array Containers

// listOf - list that contains elements in an order
// with explicit typing:
val courses1: List<String> = listOf("Intro to everything", "Math", "Physics")
// with implicit typing:
val courses2 = listOf("Intro to everything", "Math", "Physics")
// implicit typing combined with mistakes may be dangerous since lists
// may contain values of different types:
val courses0 = listOf("Intro to everything", "Math", 666, 9.4)

// elements are accessed with index::
courses1[1]
courses1.get(1)

// number of elements in a list:
courses1.size

// is an element in the list?
"Math" in courses1
courses1.contains("Mathem")

// listOf() constructs an immutable list, elements can't be
// replaced, removed or added.
// To create a mutable list, use mutableListOf():
val courses3 = mutableListOf("Intro to everything", "Math", "Physics")
courses3.add("Metaphysics")
courses3
courses3[1] = "Calculus"
courses3
courses3.remove("Calculus")
courses3
if ("Poetry" !in courses3) {
    courses3.add("Poetry")
}
courses3
courses3.add("Poetry")
courses3

val c1 = mutableListOf(1,2,3)
c1.add(4)
c1

// to concatenate lists:
courses1 + courses3

// lists work with other collection-like (iterable) constructs:
val intlist = (1..10).toList()
val clist = "In Xanadu".toList()
// and one can chain method calls: .reversed() returns a String and
// toList() is applied to that String
"In Xanadu".reversed().toList()

// one can do this in steps, too
val mystring1 = "In Xanadu"
val mystring2 = mystring1.reversed()
val mylist1 = mystring2.toList() // "In Xanadu".reversed().toList()
mylist1

// a list may contain a list:
val lengths = listOf(listOf(3,4,5), listOf(4,5), listOf(8,6,9,10))
lengths[1][0]

// Array is like a list, but its size never changes after creation,
// and it is always mutable
// As to the usage, good practice is to prefer using lists over arrays everywhere
// except for performance critical parts of your code (the reasoning is the same to that for Java)
val courses4 = arrayOf("Intro to everything", "Math", "Physics")
courses4[2] = "Biology"

// 5 - iteration, loops

// iteration (looping) over elements of a list or array
// see https://kotlinlang.org/docs/control-flow.html#for-loops
for (k in courses4) {
    println(k)
}

// iterate with index
for (i in courses4.indices) {
    println("course ${i+1}: ${courses4[i]}")
}

// iterate with index and element
for((i,k) in courses4.withIndex()) {
    println("course ${i+1}: $k")
}

// range in iteration
for(i in 0..10) {
    println("$i, ${i*i}")
}

for(c in 'z' downTo 'a') {
    print(c)
}

// while works as expected - if condition evaluates to true,
// the body is executed after which condition is evaluated again:
var i = 0
while(i <= 10) {
    println("$i, ${i*i}")
    i++
}

// In do-while loop the body is executed before evaluating the condition.
// This means that the body is always executed at least once.
do {
    println("Huh!")
} while (i > 50)

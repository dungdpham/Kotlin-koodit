fun second() {
    println("Hello Kotlin")
}

fun third() {
    println("Give me a string?: ")
    val stringInput = readLine()
    println("You entered: $stringInput")
}

fun main(args: Array<String>) {
    println("Parameters are: " + args.joinToString())
    second()
    third()
}
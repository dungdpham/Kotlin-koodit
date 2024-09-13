
open class Queue<E>(val capacity: Int = 3) {
    var data: List<E> = listOf()
        internal set
    val length: Int
        get() = data.size

    open fun enqueue(element: E) =
        if (length < capacity) { data += listOf(element); true } else { false }
    fun dequeue(): E? =
        if (data.isEmpty()) { null }
        else { val e = data[0]; data = data.drop(1); e }
}

class PriorityQueue<E: Comparable<E>>(capacity: Int = 3): Queue<E>(capacity) {
    override fun enqueue(element: E) =
        if (length < capacity) {
            data = data.filter { it <=  element } + listOf(element) + data.filter { it > element }
            true
        } else { false }
}

data class CourseRecord1(val name: String, val yearCompleted: Int, val credits: Int, val grade: Double): Comparable<CourseRecord1> {
    override fun compareTo(other: CourseRecord1) =
        if (this.credits < other.credits) 1
        else if (this.credits > other.credits) -1
        else -this.grade.compareTo(other.grade)
}

data class PrioExample(val pri: Int, val value: String): Comparable<PrioExample> {
    override fun compareTo(other: PrioExample): Int {
        return this.pri.compareTo(other.pri)
    }
}

fun <T> some(e: T, n: Int = 3): List<T> {
    val list = mutableListOf<T>()
    (1..n).forEach { _ -> list.add(e) }
    return list
}

fun <T> some1(e: T, n: Int = 3) = (1..n).map { e }

fun main() {
    val q1 = Queue<String>()
    listOf("C", "C++", "Clu", "Cobol").forEach { q1.enqueue(it) }
    while (true) {
        val e = q1.dequeue() ?: break
        println(e)
    }

    val q2 = PriorityQueue<String>(4)
    listOf("C", "Java", "Fortran", "C++", "Clu", "Cobol").forEach { q2.enqueue(it) }
    while (true) {
        val e = q2.dequeue() ?: break
        println(e)
    }

    val q4 = PriorityQueue<PrioExample>(4)
    listOf(PrioExample(3, "a"),
        PrioExample(3, "b"),
        PrioExample(1, "c"),
        PrioExample(5, "d"),
        PrioExample(3, "e"),
        PrioExample(1, "f")).forEach { q4.enqueue(it) }
    while (true) {
        val e = q4.dequeue() ?: break
        println(e)
    }

    val courses = listOf(
        CourseRecord1("Metaphysics", 1985, 10, 4.0),
        CourseRecord1("Physics", 1986, 3, 1.0),
        CourseRecord1("Modern Physics", 1988, 5, 3.0),
        CourseRecord1("Classical Physics", 1988, 5, 5.0),
        CourseRecord1("Real Analysis 1", 1989, 5, 4.0)
    )

    val q3 = PriorityQueue<CourseRecord1>(4)
    courses.forEach { q3.enqueue(it) }
    while (true) {
        val e = q3.dequeue() ?: break
        println(e)
    }

    println(some1("Kotlin"))
    println(some1(123))
}
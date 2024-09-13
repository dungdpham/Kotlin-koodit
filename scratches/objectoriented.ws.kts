
class Car(val maxSpeed: Double = 120.0, val gasolineCapacity: Double = 50.0) {
    var gasolineLevel: Double = 0.0
        private set
    var speed: Double = 0.0
        private set

    fun fillTank() {
        gasolineLevel = gasolineCapacity
    }

    fun accelerate() {
        val newSpeed = minOf(speed + 1.0, maxSpeed)
        val gasolineNeeded = (newSpeed - speed) * 0.1
        if (gasolineLevel >= gasolineNeeded) {
            gasolineLevel -= gasolineNeeded
            speed = newSpeed
        }
    }

    fun decelerate() {
        speed = maxOf(speed - 1.5, 0.0)
    }
}

val mycar = Car()
mycar.fillTank()
(1..100).forEach { mycar.accelerate() }
mycar.speed
mycar.gasolineLevel
while (mycar.speed > 0) {
    mycar.decelerate()
}
mycar.speed

// create class Human:
// - it is 'open'; can be inherited from
// - two properties ('id' and 'name') are declared in primary constructor
// - primary constructor has additional parameter initialAge
// - property 'age' is declated inside the class definition
// - objects of class Human have method toString()
open class Human(private val id: Int, val name: String, initialAge: Int = 0) {
    var age: Int = maxOf(0, initialAge)
        set(value) { field = if(value > field) value else field }

    override fun toString() = "$name ($id), $age years"
}

val people = listOf(
    Human(12, "Jill", 22),
    Human(13, "John", 19),
    Human(18, "Joe", 12),
    Human(22, "Boris"),
    Human(23, "Mario", -666)
)

people.filter { it.age >= 18 }
people.count { it.age >= 18 }

val aver = if(people.isNotEmpty()) people.map { it.age }.sum().toDouble() / people.count() else null
aver

class CourseRecord(val code: String, val credits: Int = 5, val grade: Int) {
    override fun equals(other: Any?) = (other is CourseRecord) && (code == other.code)
    override fun hashCode() = code.hashCode()
}

class Student(id: Int, name: String, initialAge: Int): Human(id, name, initialAge) {
    private val courses = mutableSetOf<CourseRecord>()
    val credits: Int
        get() = courses.map { it.credits }.sum()
    val average: Double?
        get() = if(credits == 0) null else courses.map { it.credits * it.grade }.sum().toDouble() / credits

    fun addCourse(course: CourseRecord) {
        if(course.credits > 0 && course.grade > 0)
            courses.add(course)
    }
    fun getCourses() = courses.toSet()

    override fun toString(): String {
        return "${super.toString()}, $credits credits"
    }
}

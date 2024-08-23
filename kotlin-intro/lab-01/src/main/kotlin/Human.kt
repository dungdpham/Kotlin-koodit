open class Human(name: String, age: Int) {
    var name = name
        private set
    var age = age
        private set

    fun getOlder() {
        age += 1
    }
}

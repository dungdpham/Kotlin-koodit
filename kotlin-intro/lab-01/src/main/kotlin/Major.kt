class Major(name: String) {
    var name = name
        private set
    private val _students = mutableListOf<Student>()
    val students: List<Student>
        get() = _students

    fun addStudent(student: Student) {
        _students.add(student)
    }

    fun stats(): Triple<Double, Double, Double> {
        val averages: List<Double> = _students.map { it.weightedAverage() }
        return Triple(averages.min(), averages.max(), averages.average())
    }

    fun stats(courseName: String): Triple<Double, Double, Double> {
        val filteredGrades = _students
            .flatMap { it.courses }
            .filter { it.name == courseName }
            .map { it.grade }
        return Triple(filteredGrades.min(), filteredGrades.max(), filteredGrades.average())
    }
}

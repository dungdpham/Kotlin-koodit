class Student(name: String, age: Int) : Human(name, age) {
    private val _courses: MutableList<CourseRecord> = mutableListOf()
    val courses: List<CourseRecord>
        get() = _courses

    fun addCourse(course: CourseRecord) {
        _courses.add(course)
    }

    fun weightedAverage(): Double {
        var gradeSum = 0.0
        var creditsSum = 0
        for (course in _courses) {
            gradeSum += course.grade*course.credits
            creditsSum += course.credits
        }
        return if (creditsSum != 0) gradeSum/creditsSum else 0.0
    }

    fun weightedAverage(year: Int): Double {
        val filteredCourses = _courses.filter { it.yearCompleted == year }
        var gradeSum = 0.0
        var creditsSum = 0.0
        for (course in filteredCourses) {
            gradeSum += course.grade*course.credits
            creditsSum += course.credits
        }
        return if (creditsSum != 0.0) gradeSum/creditsSum else 0.0
    }

    fun minMaxGrades(): Pair<Double, Double> {
        val grades: List<Double> = _courses.map { it.grade }
        return Pair(grades.min(), grades.max())
    }
}

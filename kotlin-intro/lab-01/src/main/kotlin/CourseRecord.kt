class CourseRecord(name: String, year: Int, credits: Int, grade: Double) {
    var name = name
        private set
    var yearCompleted = year
        private set
    var credits = credits
        private set
    var grade = grade
        private set

    init {
        require(credits > 0) {"'credits' value must be greater than 0"}
        require(grade >= 0.0) {"'grade' value must be greater or equal to 0"}
    }
}

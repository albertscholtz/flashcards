fun main() {
    val studentsMarks = mutableMapOf<String, Int>()
    while (true) {
        val name = readln()
        if (name == "stop") break
        val mark = readln().toInt()
        if (name !in studentsMarks) studentsMarks[name] = mark
    }
    println(studentsMarks)
}
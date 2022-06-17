fun main() {
    val line: String? = readlnOrNull() ?: error("No lines read")
    // write your code here. Do not change line above
    println(line!!.length ?: -1)
}
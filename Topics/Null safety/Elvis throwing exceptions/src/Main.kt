fun main() {
    val test: String? = null
    println(null == false)
    println(null == true)
    println(null != false)
    println(null != true)
    println(test?.isEmpty() == false)
    println(test?.isEmpty() == true)
    println(test?.isEmpty() != false)
    println(test?.isEmpty() != true)
}
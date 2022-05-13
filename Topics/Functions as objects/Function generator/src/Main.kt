fun identity(i: Int): Int = i
fun half(i: Int): Int = i / 2
fun zero(i: Int): Int = 0

fun generate(functionName: String): (Int) -> Int {
    when (functionName) {
        "identity" -> return ::identity
        "half" -> return ::half
        "zero" -> return ::zero
        else -> return ::zero
    }
}
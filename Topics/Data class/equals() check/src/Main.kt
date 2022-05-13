data class Client(val name: String, val age: Int, val balance: Int) {

    override fun equals(other: Any?): Boolean {
        other as Client
        return other.name == name && other.age == age
    }
}

fun main() {
    val c1 = Client(readln(), readln().toInt(), readln().toInt())
    val c2 = Client(readln(), readln().toInt(), readln().toInt())
    println(c1 == c2)
}
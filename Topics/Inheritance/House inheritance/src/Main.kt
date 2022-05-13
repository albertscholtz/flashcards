import kotlin.math.abs

fun main() {
    val rooms = readln().toInt()
    val price = readln().toInt()
    val house = House(rooms, price)
    println(abs(totalPrice(house).toInt()))
}

fun totalPrice(house: House): Double {
    return when {
        house.price <= 0 -> 0.0
        house.rooms <= 1 -> house.price.toDouble()
        house.rooms in 2..3 -> house.price * 1.2
        house.rooms == 4 -> house.price * 1.25
        house.rooms in 5..7 -> house.price * 1.4
        house.rooms in 8..10 -> house.price * 1.6
        else -> house.price * 1.6
    }
}

class House(val rooms: Int, val price: Int) { }
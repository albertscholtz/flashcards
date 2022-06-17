val lambda: (Long, Long) -> Long = {left, right ->
    var total = right
    for (i in left until right) {
        total *= i
    }
    total
}
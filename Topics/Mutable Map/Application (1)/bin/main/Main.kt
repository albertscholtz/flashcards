fun addUser(userMap: Map<String, String>, login: String, password: String): MutableMap<String, String> {
    val users = userMap.toMutableMap()
    users.putIfAbsent(login, password).let { if (!it.isNullOrEmpty()) println("User with this login is already registered!") }
    return users
}
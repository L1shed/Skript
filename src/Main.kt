fun main() {
    while (true) {
        print(">> ")
        val line = readLine() ?: continue


        for ((pattern, action) in matches) {
            val abstract = Matcher.abstract(line)
            if (abstract.first == pattern) {
                action(abstract.second)
                break
            }
        }
    }
}

val variables = mutableMapOf<String, Any>(
    "test" to "Hello, World!",
)

val matches = mapOf<String, (args: Array<String>) -> Unit >(
    "send %string%" to { (it) ->
        println(it)
    },

    "set %objects% to %objects%" to { (obj1, obj2) ->
        variables[obj1] = obj2
        println("Set $obj1 to $obj2")
    },
)


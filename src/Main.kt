import type.Object
import type.Variable
import java.io.File

fun main() {
    while (true) {
        print(">> ")
        val line = readLine() ?: continue


        for ((pattern, action) in matches) {
            val abstract = Matcher.abstract(line, pattern)
            if (!abstract.first) continue

            action(abstract.second)
        }
    }
}

val variables = mutableMapOf<String, Object>(
    "test" to type.String("Hello, World!"),
)

val matches = mapOf<String, (args: Array<Object>) -> Unit >(
    "(send|message) %string%" to { (it) ->
        println(it)
    },

    "set %variable% to %object%" to { (obj1, obj2) ->
        variables[(obj1 as Variable).ref] = obj2
        println("DEBUG: Set $obj1 to $obj2")
    },

    "add %number% to %variable%" to { (num, obj) ->
        println("$num!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    },

    "list variables" to { _ ->
        println(variables)
    },
)

fun run(file: File) {
    file.forEachLine {
        for ((pattern, action) in matches) {
            val abstract = Matcher.abstract(it, pattern)
            if (!abstract.first) continue

            action(abstract.second)
        }
    }
}


import type.Number
import type.Object
import type.Variable
import java.io.File

fun main() {
//    run(File("./scripts/print.sk"))

//    Thread.sleep(1000000)

    while (true) {
        print(">> ")
        val line = readLine() ?: continue

        Parser.parseLine(line)
    }
}

val variables = mutableMapOf<String, Object<*>>(
    "test" to type.String("Hello, World!"),
)

val matches = mapOf<String, (args: Array<Object<*>>) -> Unit >(
    "(send|message) %string%" to { (it) ->
        println(it)
    },

    "set %variable% to %object%" to { (var1, obj) ->
        variables[(var1 as Variable).ref] = obj
//        println("DEBUG: Set $obj1 to $obj2")
    },

    "add %number% to %variable%" to { (num, var1) ->
        val obj = variables[(var1 as Variable).ref] as Number
        obj.value = obj.value.toFloat() +
                (num as Number).value.toFloat()
        println("$num!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    },

    "list variables" to { _ ->
        println(variables)
    },
)

fun run(file: File) {
    // TODO: Tabulated conds handling (=1)
    if (file.extension != "sk")
        println("rename file to .sk")

    file.forEachLine {
        Parser.parseLine(it)
    }
}


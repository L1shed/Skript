package type

import kotlin.Number
import kotlin.String

enum class Types {
    OBJECT,
    STRING,
}

open class Object(
    val value: Any,
) {
    override fun toString(): String {
        return value.toString()
    }
}

class String(
    value: String
) : Object(value)

class Number(
    value: Number
) : Object(value)

class Variable(
    value: Object,
    val ref: String
) : Object(value)
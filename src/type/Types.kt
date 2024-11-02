package type

import kotlin.Number
import kotlin.String

enum class Types {
    OBJECT,
    STRING,
}

open class Object<T>(
    var value: T,
) {
    override fun toString(): String {
        return value.toString()
    }
}

class String(
    value: String
) : Object<String>(value)

class Number(
    value: Float
) : Object<Float>(value)

class Variable(
    value: Object<*>,
    val ref: String
) : Object<Object<*>>(value)
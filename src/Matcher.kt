import type.*

object Matcher {
    private enum class MatchType {
        BRACES,
        QUOTES,
    }

    fun abstract(
        string: String,
        pattern: String
    ): Pair<Boolean, Array<Object<*>>> { // TODO: split Pair into 2 functions, for optimization
        // TODO: expected type(s)
        //  - type reusability
        val patternTypes = Parser.parseTypes(pattern)

        var abstract = string
        val matches = mutableListOf<Object<*>>()

        var start: Int? = null
        var matchType: MatchType? = null
        var tempNum = ""

        fun replaceWithNeededType(substring: String): String? {
            if (patternTypes.size <= matches.size)
                return null
            val neededType = patternTypes[matches.size]
            abstract = abstract.replace(substring, "%$neededType%")
            return neededType
        }

        fun trySaveNum() {
            if (tempNum.isNotBlank()) {
                replaceWithNeededType(tempNum)
                tempNum = tempNum.replace("_", "")
                matches.add(Number(tempNum.toFloat()))
                tempNum = ""
            }
        }

        for ((i, c) in string.withIndex()) {
            if (c == '{' || (c == '"' && matchType == null)) {
                start = i
                matchType = if (c == '{') MatchType.BRACES else MatchType.QUOTES
            } else if (c == '}' || c == '"') {
                val match = string.substring(start!! + 1, i)

                val neededType = replaceWithNeededType(string.substring(start, i + 1)) ?: return Pair(false, arrayOf())

                val value =
                    if (matchType == MatchType.BRACES) {
                        if (neededType == "variable") {
                            Variable(String(match), match)
                        } else {
                            variables[match] ?: String("<none>")
                        }
                    } else {
                        String(match)
                    }

                matches.add(value)

                start = null
                matchType = null
            } else if (c.isDigit() || c == '_') {
                if (matchType == null)
                    tempNum += c
            } else {
                trySaveNum()
            }
        }
        trySaveNum()

        println("Abstract: $abstract")
        println("Matches: $matches")

        var matched = abstract.matches(pattern.toRegex())
                && patternTypes.size == matches.size

        for ((i, type) in patternTypes.withIndex()) {
            if (!matched) return Pair(false, arrayOf())

            matched = when (type) {
                "object" -> true
                "string" -> matches[i] is type.String
                "number" -> matches[i] is type.Number
                "variable" -> matches[i] is Variable
                else -> throw Exception("Invalid type")
            }
        }

        return Pair(matched, matches.toTypedArray())
    }
}
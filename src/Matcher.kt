import type.*

object Matcher {
    private enum class MatchType {
        BRACES,
        QUOTES,
    }

    fun abstract(
        string: String,
        pattern: String
    ): Pair<Boolean, Array<Object>> { // TODO: split Pair into 2 functions, for optimization
        // TODO: expected type(s)
        //  - type reusability
        val patternTypes = Parser.parseTypes(pattern)

        var abstract = string
        val matches = mutableListOf<Object>()

        var start: Int? = null
        var matchType: MatchType? = null
        for ((i, c) in string.withIndex()) {
            if (c == '{' || (c == '"' && matchType == null)) {
                start = i
                matchType = if (c == '{') MatchType.BRACES else MatchType.QUOTES
            } else if (c == '}' || c == '"') {
                val match = string.substring(start!! + 1, i)

                if (patternTypes.size <= matches.size)
                    return Pair(false, arrayOf())
                val neededType = patternTypes[matches.size]

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


//                val typeStr =
//                    when (matchType) {
//                        MatchType.BRACES -> neededType
//                        MatchType.QUOTES -> "string"
//                        else -> throw Exception("Invalid match type")
//                    }

                abstract = abstract.replace(string.substring(start, i + 1), "%$neededType%")


                matches.add(
                    value
                )

//                if (typeStr != neededType) {
//                    return Pair(false, arrayOf())
//                }

                start = null
                matchType = null
            }
        }

        println("Abstract: $abstract")
        println("Matches: $matches")

        var matched = abstract.matches(pattern.toRegex())
                && patternTypes.size == matches.size

        for ((i, type) in patternTypes.withIndex()) {
            if (!matched) return Pair(false, arrayOf())

            matched = when (type) {
                "object" -> true
                "string" -> matches[i] is type.String
                "number" -> matches[i] is Number
                "variable" -> matches[i] is Variable
                else -> throw Exception("Invalid type")
            }
        }

        return Pair(matched, matches.toTypedArray())
    }
}
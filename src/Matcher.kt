object Matcher {
    private enum class MatchType {
        BRACES,
        QUOTES,
    }
    fun abstract(string: String): Pair<String, Array<String>> { // TODO: split Pair into 2 functions, for optimization
        // TODO: expected type(s)
        //  - type reusability
        var abstract = string
        val matches = mutableListOf<String>()

        var start: Int? = null
        var type: MatchType? = null
        for ((i,c) in string.withIndex()) {
            if (c == '{' || (c == '"' && type == null)) {
                start = i
                type = if (c == '{') MatchType.BRACES else MatchType.QUOTES
            } else if (c == '}' || c == '"') {
                val match = string.substring(start!!+1, i)
                val value = variables[match] as? String ?: "<none>"
                val typeStr = "%${
                    when(type) {
                        MatchType.BRACES -> {
                            when(variables[match] ?: "<none>") {
                                is String -> "string"
                                is Number -> "number"
                                else -> "objects"
                            }
                        }
                        MatchType.QUOTES -> "string"
                        else -> throw Exception("Invalid match type")
                    }
                }%"

                abstract = abstract.replace(string.substring(start, i+1), typeStr)


                matches.add(
                    if (type == MatchType.BRACES) value else match
                )

                start = null
                type = null
            }
        }

        println("Abstract: $abstract")
        println("Matches: $matches")

        return Pair(abstract, matches.toTypedArray())
    }

    private fun between(string: String, start: String, end: String): List<String> {
        val regex = "\\$start(.*?)\\$end".toRegex()
        val matches = regex.findAll(string).map { it.value }.toList()
        return matches
    }
}
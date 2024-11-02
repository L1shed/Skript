object Parser {
    fun parseLine(line: String) {
        if (line.isEmpty()) return
        for ((pattern, action) in matches) {
            val abstract = Matcher.abstract(line, pattern)
            if (!abstract.first) continue

            action(abstract.second)
            return
        }
    }

    fun parseTypes(pattern: String): List<String> {
        val types = mutableListOf<String>()
        var start: Int? = null

        for ((i, c) in pattern.withIndex()) {
            if (c == '%' && start == null) {
                start = i
            } else if (c == '%' && start != null) {
                types.add(pattern.substring(start + 1, i))
                start = null
            }
        }
        return types
    }
}
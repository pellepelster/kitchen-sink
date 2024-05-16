package io.pelle.jvm.time

enum class ParseResultStatus {
    ok, failed, diff
}

data class FormatParseResults(val timeFormat: TimeFormat, val parseResults: List<ParseResult>)

data class ParseResult(
    val name: String,
    val status: ParseResultStatus,
    val value: String? = null,
    val error: String? = null,
    val diff: Long? = null
)

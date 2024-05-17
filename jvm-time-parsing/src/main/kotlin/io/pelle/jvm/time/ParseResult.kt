package io.pelle.jvm.time

enum class ParseResultStatus {
    ok, failed, diff
}

data class ParseResults(val timeFormat: TimeFormat, val parseResults: List<ParseResult>)

data class ParseResult(
    val name: String,
    val status: ParseResultStatus,
    val value: String? = null,
    val error: String? = null,
    val diff: Long? = null
)

data class ParseResultColumns(
    val name: String,
    val successTotal: Int,
    val diffTotal: Int,
)

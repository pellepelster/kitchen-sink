package io.pelle.jvm.time

enum class ParseResultStatus {
    ok, failed, diff
}

data class ParseResults(val timeFormat: TimeFormat, val parseResults: List<ParseResult>) {
    val hasSuccess: Boolean = parseResults.any { it.status == ParseResultStatus.ok }

}

data class ParseResult(
    val name: String,
    val status: ParseResultStatus,
    var benchmark: Long,
    var benchmarkHeatMapColor: String? = null,
    val value: String? = null,
    val error: String? = null,
    val diff: Long? = null
)

data class ParseResultColumns(
    val name: String,
    val successTotal: Int,
    val diffTotal: Int,
)

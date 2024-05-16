package io.pelle.jvm.time

enum class ParserStatus {
    ok, failed, diff
}

data class ParserResult(
    val name: String,
    val status: ParserStatus,
    val value: String? = null,
    val error: String? = null
)

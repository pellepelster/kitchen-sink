package io.pelle.jvm.time

import java.time.format.DateTimeFormatter

const val expectedTimestamp = 1715872927L

fun safeParse(name: String, parse: () -> Long): ParseResult {
    return try {
        val value = parse.invoke()

        if (value == expectedTimestamp) {
            ParseResult(name, ParseResultStatus.ok, value.toString())
        } else {
            ParseResult(
                name = name,
                status = ParseResultStatus.diff,
                value = value.toString(),
                diff = expectedTimestamp - value
            )
        }
    } catch (e: Exception) {
        ParseResult(name = name, status = ParseResultStatus.failed, error = e.message)
    }
}

interface Parser {
    fun parse(now: String): List<ParseResult>
}
package io.pelle.jvm.time.time

import io.pelle.jvm.time.BaseTimeParser
import io.pelle.jvm.time.ParseResult
import io.pelle.jvm.time.Parser
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter

class SimpleDateFormat : Parser {
    override fun parseToEpochSecond(now: String): Long {
        return Instant.from(DateTimeFormatter.ISO_DATE.parse(now)).epochSecond
    }

    override fun parse(now: String): List<ParseResult> {
        TODO("Not yet implemented")
    }
}
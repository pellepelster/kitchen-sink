package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.TimeFormat
import io.pelle.jvm.time.parseAndBenchmark
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTime : Parser {
    override fun parse(timeFormat: TimeFormat) =
        listOf(parseAndBenchmark("java.time.LocalDateTime.parse(String)", timeFormat) {
            LocalDateTime.parse(timeFormat.now).toEpochSecond(ZoneOffset.UTC)
        })
}
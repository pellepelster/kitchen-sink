package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.TimeFormat
import io.pelle.jvm.time.parseAndBenchmark

class JodaLocalDateTime : Parser {
    override fun parse(timeFormat: TimeFormat) =
        listOf(parseAndBenchmark("org.joda.time.LocalDateTime.parse(String)", timeFormat) {
            org.joda.time.LocalDateTime.parse(timeFormat.now).toDateTime().millis / 1000
        })
}
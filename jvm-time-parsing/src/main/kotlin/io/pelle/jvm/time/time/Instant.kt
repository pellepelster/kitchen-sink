package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.TimeFormat
import io.pelle.jvm.time.parseAndBenchmark
import java.time.Instant

class Instant : Parser {
    override fun parse(timeFormat: TimeFormat) =
        listOf(parseAndBenchmark("java.time.Instant.parse(String)", timeFormat) {
            Instant.parse(timeFormat.now).epochSecond
        })
}
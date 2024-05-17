package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.TimeFormat
import io.pelle.jvm.time.parseAndBenchmark

class UtilDate : Parser {
    override fun parse(timeFormat: TimeFormat) = listOf(parseAndBenchmark("java.util.Date.parse(String)", timeFormat) {
        java.util.Date.parse(timeFormat.now)
    })
}
package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.TimeFormat
import io.pelle.jvm.time.parseAndBenchmark

class SqlDate : Parser {
    override fun parse(timeFormat: TimeFormat) = listOf(parseAndBenchmark("java.sql.Date.parse(String)", timeFormat) {
        java.sql.Date.parse(timeFormat.now)
    })
}
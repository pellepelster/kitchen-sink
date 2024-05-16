package io.pelle.jvm.time.time

import io.pelle.jvm.time.TimeParser
import java.util.*

class SqlDate : TimeParser {
    override fun parseToEpochSecond(now: String): Long {
        return Date.parse(now)
    }
}
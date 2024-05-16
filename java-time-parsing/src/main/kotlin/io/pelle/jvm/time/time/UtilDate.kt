package io.pelle.jvm.time.time

import java.util.*

class UtilDateTimeParser : TimeParser {
    override fun parse(now: String): Long {
        return Date.parse(now)
    }
}
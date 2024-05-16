package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse

class SqlDate : Parser {
    override fun parse(now: String) = listOf(safeParse("java.sql.Date.parse") {
        java.sql.Date.parse(now)
    })
}
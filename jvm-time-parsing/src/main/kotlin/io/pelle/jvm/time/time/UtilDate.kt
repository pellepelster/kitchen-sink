package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse

class UtilDate : Parser {
    override fun parse(now: String) = listOf(safeParse("java.util.Date.parse(String)") {
        java.util.Date.parse(now)
    })
}
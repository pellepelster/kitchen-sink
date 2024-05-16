package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse

class JodaLocalDateTime : Parser {
    override fun parse(now: String) = listOf(safeParse("org.joda.time.LocalDateTime.parse") {
        org.joda.time.LocalDateTime.parse(now).toDateTime().millis / 1000
    })
}
package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse
import java.time.LocalDateTime
import java.time.ZoneOffset

class JodaLocalDateTime : Parser {
    override fun parse(now: String) = listOf(safeParse("LocalDateTime.parse") {
        LocalDateTime.parse(now).toEpochSecond(ZoneOffset.UTC)
    })
}
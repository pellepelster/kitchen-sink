package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTime : Parser {
    override fun parse(now: String) = listOf(safeParse("java.time.LocalDateTime.parse(String)") {
        LocalDateTime.parse(now).toEpochSecond(ZoneOffset.UTC)
    })
}
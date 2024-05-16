package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse
import java.time.Instant

class Instant : Parser {
    override fun parse(now: String) = listOf(safeParse("java.time.Instant.parse(String)") {
        Instant.parse(now).epochSecond
    })
}
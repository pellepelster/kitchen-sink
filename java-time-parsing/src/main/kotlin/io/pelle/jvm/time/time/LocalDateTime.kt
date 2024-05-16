package io.pelle.jvm.time.time

import io.pelle.jvm.time.BaseTimeParser
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateBaseTime : BaseTimeParser() {
    override fun parseToEpochSecond(now: String): Long {
        return LocalDateTime.parse(now).toEpochSecond(ZoneOffset.UTC)
    }
}
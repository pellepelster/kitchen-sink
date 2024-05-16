package io.pelle.jvm.time.time

import io.pelle.jvm.time.Parser
import io.pelle.jvm.time.safeParse
import java.time.Instant
import java.time.format.DateTimeFormatter

class DateTimeFormatter : Parser {
    override fun parse(now: String) = listOf(
        "DateTimeFormatter.BASIC_ISO_DATE.parse(String)" to DateTimeFormatter.BASIC_ISO_DATE,
        "DateTimeFormatter.ISO_LOCAL_DATE.parse(String)" to DateTimeFormatter.ISO_LOCAL_DATE,
        "DateTimeFormatter.ISO_OFFSET_DATE.parse(String)" to DateTimeFormatter.ISO_OFFSET_DATE,
        "DateTimeFormatter.ISO_DATE.parse(String)" to DateTimeFormatter.ISO_DATE,
        "DateTimeFormatter.ISO_LOCAL_TIME.parse(String)" to DateTimeFormatter.ISO_LOCAL_TIME,
        "DateTimeFormatter.ISO_OFFSET_TIME.parse(String)" to DateTimeFormatter.ISO_OFFSET_TIME,
        "DateTimeFormatter.ISO_TIME.parse(String)" to DateTimeFormatter.ISO_TIME,
        "DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(String)" to DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        "DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(String)" to DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        "DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(String)" to DateTimeFormatter.ISO_ZONED_DATE_TIME,
        "DateTimeFormatter.ISO_DATE_TIME.parse(String)" to DateTimeFormatter.ISO_DATE_TIME,
        "DateTimeFormatter.ISO_ORDINAL_DATE.parse(String)" to DateTimeFormatter.ISO_ORDINAL_DATE,
        "DateTimeFormatter.ISO_WEEK_DATE.parse(String)" to DateTimeFormatter.ISO_WEEK_DATE,
        "DateTimeFormatter.ISO_INSTANT.parse(String)" to DateTimeFormatter.ISO_INSTANT,
        "DateTimeFormatter.RFC_1123_DATE_TIME.parse(String)" to DateTimeFormatter.RFC_1123_DATE_TIME,
    ).map {
        safeParse(it.first) {
            Instant.from(it.second.parse(now)).epochSecond
        }
    }
}
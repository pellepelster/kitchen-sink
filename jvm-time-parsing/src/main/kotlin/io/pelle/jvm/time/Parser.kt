package io.pelle.jvm.time

import kotlin.system.measureNanoTime

fun parseAndBenchmark(name: String, timeFormat: TimeFormat, parseDateTime: () -> Long): ParseResult {
    val benchmark = benchmark(name, parseDateTime)

    return try {
        val value = parseDateTime.invoke()

        if (value == expectedTimestamp) {
            println("parsed '${timeFormat.now}' with ${name} to ${value}")
            ParseResult(name, ParseResultStatus.ok, benchmark, value.toString())
        } else {
            val diff = expectedTimestamp - value
            println("parse '${timeFormat.now}' with ${name} had a diff of ${diff}")
            ParseResult(
                name = name,
                benchmark = benchmark,
                status = ParseResultStatus.diff,
                value = value.toString(),
                diff = diff
            )
        }
    } catch (e: Exception) {
        println("failed to parse '${timeFormat.now}' with ${name}, error was '${e.message}'")
        ParseResult(name = name, status = ParseResultStatus.failed, benchmark = benchmark, error = e.message)
    }
}

fun benchmark(name: String, parseDateTime: () -> Long): Long {

    for (i in 1..benchmarkPreWarmIterations) {
        measureNanoTime {
            try {
                parseDateTime()
            } catch (e: Exception) {
            }
        }
    }

    var totalTimeInNanos = 0L

    for (i in 1..benchmarkIterations) {
        val timeInNanos = measureNanoTime {
            try {
                parseDateTime()
            } catch (e: Exception) {
            }
        }

        totalTimeInNanos += timeInNanos
    }

    return totalTimeInNanos / benchmarkIterations
}

interface Parser {
    fun parse(timeFormat: TimeFormat): List<ParseResult>
}
package io.pelle.jvm.time

import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import io.pelle.jvm.time.time.*
import java.io.File
import java.io.FileWriter


fun parseFormats(file: String) = Parser::class.java.getResource(file)?.readText()?.lines()?.map {
    val parts = it.split("\t")
    if (parts.size != 2) {
        throw RuntimeException("invalid line '${it}' in file: ${file}")
    }
    TimeFormat(parts[1], parts[0])
} ?: throw RuntimeException("file not found: $file")

fun checkParsers(files: List<String>, parsers: List<Parser>) = files.flatMap { file ->
    parseFormats("/formats/${file}").map { timeFormat ->
        ParseResults(timeFormat, parsers.flatMap {
            it.parse(timeFormat)
        })
    }
}

fun writeReport(results: List<ParseResults>) {

    val cfg = Configuration(Configuration.VERSION_2_3_32)
    cfg.setClassForTemplateLoading(Parser::class.java, "/templates")
    cfg.defaultEncoding = "UTF-8"
    cfg.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER

    val min = results.flatMap { it.parseResults }.map { it.benchmark }.min() - 1
    val max = results.flatMap { it.parseResults }.map { it.benchmark }.max() + 1

    println("min parse time: ${min}, max parse time: ${max}")

    val totalRange = (max - min)
    val bucketSize = (totalRange / benchmarkHeatMapColors.size).toLong()
    var currentBucketStart = min

    val heatMap = benchmarkHeatMapColors.indices.map {
        val range = if (it == benchmarkHeatMapColors.size - 1) {
            currentBucketStart..Long.MAX_VALUE
        } else {
            currentBucketStart..currentBucketStart + bucketSize
        }

        currentBucketStart += bucketSize
        range to benchmarkHeatMapColors[it]
    }.toMap()

    heatMap.forEach { t, u ->
        println("mapping range ${t} to color ${u}")
    }

    results.forEach {
        it.parseResults.forEach { result ->
            result.benchmarkHeatMapColor = heatMap.entries.firstOrNull { it.key.contains(result.benchmark) }?.value
        }
    }

    val columns = results.first().parseResults.map { first ->
        ParseResultColumns(
            first.name,
            results.flatMap { it.parseResults }
                .filter { it.name == first.name && it.status == ParseResultStatus.ok }.size,
            results.flatMap { it.parseResults }
                .filter { it.name == first.name && it.status == ParseResultStatus.diff }.size
        )
    }
    cfg.getTemplate("report.ftl").process(
        mapOf(
            "expectedTimestamp" to expectedTimestamp.toString(),
            "benchmarkIterations" to benchmarkIterations.toString(),
            "results" to results,
            "columns" to columns
        ),
        FileWriter(File("report.html"))
    )
}

fun main() {
    val parsers: List<Parser> =
        listOf(JodaLocalDateTime(), Instant(), LocalDateTime(), DateTimeFormatter(), UtilDate(), SqlDate())

    val results = checkParsers(listOf("datetimes.txt", "times.txt", "dates.txt"), parsers)

    writeReport(results)
}

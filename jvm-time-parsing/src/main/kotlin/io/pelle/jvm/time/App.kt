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
    parseFormats("/formats/${file}").map { format ->
        FormatParseResults(format, parsers.flatMap {
            it.parse(format.time)
        })
    }
}

fun writeReport(results: List<FormatParseResults>) {

    val cfg = Configuration(Configuration.VERSION_2_3_32)
    cfg.setClassForTemplateLoading(Parser::class.java, "/templates")
    cfg.defaultEncoding = "UTF-8"
    cfg.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER

    //println("parsing '${format.time} with ${result.name}: status=${result.status}, value=${result.value}, error=${result.error}, diff=${result.diff}")

    cfg.getTemplate("report.ftl").process(
        mapOf(
            "expectedTimestamp" to expectedTimestamp.toString(),
            "results" to results,
            "headers" to results.first().parseResults.map { it.name }),
        FileWriter(File("report.html"))
    )
}

fun main() {
    val parsers: List<Parser> =
        listOf(JodaLocalDateTime(), Instant(), LocalDateTime(), DateTimeFormatter(), UtilDate(), SqlDate())

    val results = checkParsers(listOf("datetimes.txt", "times.txt", "dates.txt"), parsers)

    writeReport(results)
}

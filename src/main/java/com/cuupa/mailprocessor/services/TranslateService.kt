package com.cuupa.mailprocessor.services

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.LocaleUtils
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class TranslateService {

    private val translations: MutableMap<Locale, Map<String, String>> = mutableMapOf()

    private val equalRegex = "=".toRegex()

    private val dotRegex = "\\.".toRegex()

    init {
        Files.list(Paths.get("src/main/resources/locales"))
                .forEach {
                    translations[LocaleUtils.toLocale(it.toFile().name.split(dotRegex)[0])] =
                            getTranslateMappings(FileUtils.readLines(it
                                    .toFile(),
                                    StandardCharsets.UTF_8))
                }
    }

    private fun getTranslateMappings(readLines: List<String>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        readLines.forEach {
            val split = it.split(equalRegex)
            map[split[0].trim()] = split[1].trim()
        }
        return map
    }

    fun translate(toTranslate: String, locale: Locale): String {
        if (!translations.contains(locale)) {
            return toTranslate
        }
        return translations[locale]?.getOrDefault(toTranslate, toTranslate) ?: toTranslate
    }
}
package org.ssc

import java.io.*

const val csv = """
aaa,bbb,ccc,,,,
123,456,789
987,654,321
"""

fun main() {
    println("Hello World!")

    val text = "qwerirtfpasfdmniiohhlo"
    val vowelCounts = countVowels(text)
    println(vowelCounts)

    val csvTxt = csv.trimIndent()
    println("$csvTxt\n")

    val inputSteamA = ByteArrayInputStream(csvTxt.toByteArray())
    inputSteamA.use {
        val inputSteamB = cleansingPipeline(it)
        parsingPipeline(inputSteamB)
    }
}

fun countVowels(text: String): Map<Char, Int> {
    val vowels = "aeiou"
    val counts = mutableMapOf<Char, Int>().withDefault { 0 }
    for (char in text) {
        if (char in vowels) {
            counts[char] = counts.getValue(char) + 1
        }
    }
    return counts
}

private fun cleansingPipeline(inputStream: InputStream): InputStream {

    val outputStream = PipedOutputStream()
    val nextInputSteam = PipedInputStream()
    nextInputSteam.connect(outputStream)

    val reader = BufferedReader(inputStream.reader())
    reader.forEachLine {
        val l = it.split(",")
            .filter(String::isNotBlank)
            .joinToString(separator = ",")

        outputStream.write("$l\n".toByteArray())
        outputStream.flush()
    }
    outputStream.close()

    return nextInputSteam
}

private fun parsingPipeline(inputStream: InputStream) {

    val reader = BufferedReader(inputStream.reader())
    reader.forEachLine(::println)
}
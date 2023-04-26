import java.io.*

const val csv = """
aaa,bbb,ccc,,,,
123,456,789
987,654,321
"""


fun main() {

    println(csv.trimIndent())
    println()

    val inputSteamA = ByteArrayInputStream(csv.trimIndent().toByteArray())

    inputSteamA.use {
        val inputSteamB = cleansingPipeline(it)
        parsingPipeline(inputSteamB)
    }
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
    }
    outputStream.flush()
    outputStream.close()

    return nextInputSteam
}

private fun parsingPipeline(inputStream: InputStream) {

    val reader = BufferedReader(inputStream.reader())
    reader.forEachLine(::println)
}
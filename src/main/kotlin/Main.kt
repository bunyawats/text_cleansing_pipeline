import java.io.*

const val csv = """
aaa,bbb,ccc,,,,
123,456,789
987,654,321
"""


fun main(args: Array<String>) {

    println(csv.trimIndent())
    println()

    val inputSteamA = ByteArrayInputStream(csv.trimIndent().toByteArray())

    inputSteamA.use { inputSteamA ->
        val inputSteamB = cleansingPipeline(inputSteamA)
        parsingPipeline(inputSteamB)
    }
}

private fun cleansingPipeline(inputStream: InputStream): InputStream {

    val outputStream = PipedOutputStream()
    val nextInputSteam = PipedInputStream()
    nextInputSteam.connect(outputStream)

    val reader = BufferedReader(inputStream.reader())
    reader.use { reader ->
        var line = reader.readLine()
        while (line != null) {
            line = line.split(",")
                .filter(String::isNotBlank)
                .joinToString(separator = ",")

            outputStream.write(line.toByteArray())
            outputStream.write("\n".toByteArray())
            line = reader.readLine()
        }
        outputStream.flush()
        outputStream.close()
    }
    return nextInputSteam
}

private fun parsingPipeline(inputStream: InputStream) {

    val reader = BufferedReader(inputStream.reader())
    reader.use { reader ->
        var line = reader.readLine()
        while (line != null) {
            println(line)
            line = reader.readLine()
        }
    }
}
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


val categoryTranslation = mapOf(
    "PRODUCTIVITY" to "Продуктивность",
    "GAME" to "Игры",
    "TOOLS" to "Инструменты"
)

fun main() {

    // Путь к файлу CSV
    val filePath = "src/main/resources/googleplaystore.csv"

    val apps = File(filePath).readLines().drop(1).map { line ->
        val columns = parseCsvLine(line)
        println(columns)
        App(
            name = columns[0],
            category = categoryTranslation[columns[1]] ?: "",
            rating = columns[2].toDoubleOrNull() ?: 0.0,
            reviews = columns[3].toIntOrNull() ?: 0,
            size = columns[4],
            installs = installsToNumber(columns[5]),
            type = columns[6],
            price = priceToBoolean(columns[7]),
            contentRating = columns[8],
            genres = columns[9],
            lastUpdated = convertDateToISO8601(columns[10]),
            currentVer = androidVersionToApi(columns[11]),
            androidVer = androidVersionToApi(columns[12])
        )
    }


    val groupedData = apps.groupBy { it.category }

    val jsonResult = groupedData.mapValues { (_, apps) ->
        apps.map { app ->
            mapOf(
                "name" to app.name,
                "category" to app.category,
                "rating" to app.rating,
                "reviews" to app.reviews,
                "size" to app.size,
                "installs" to app.installs,
                "type" to app.type,
                "price" to app.price,
                "contentRating" to app.contentRating,
                "genres" to app.genres,
                "lastUpdated" to app.lastUpdated,
                "currentVer" to app.currentVer,
                "androidVer" to app.androidVer
            )
        }
    }

    println(jsonResult)
}

fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    var currentColumn = StringBuilder()
    var insideQuotes = false

    for (char in line) {
        when {
            char == ',' && !insideQuotes -> {
                result.add(currentColumn.toString())
                currentColumn = StringBuilder()
            }

            char == '"' -> insideQuotes = !insideQuotes
            else -> currentColumn.append(char)
        }
    }

    result.add(currentColumn.toString())
    return result
}

// Дополнительные функции преобразования
fun androidVersionToApi(androidVer: String): Int {
    val versionRegex = """(\d+\.\d+)""".toRegex()
    val matchResult = versionRegex.find(androidVer)

    return matchResult?.groupValues?.get(1)?.toDoubleOrNull()?.let {
        when {
            it >= 1.0 && it < 1.5 -> 3
            it >= 1.5 && it < 1.6 -> 4
            it >= 2.0 && it < 2.1 -> 7
            it >= 2.2 && it < 2.3 -> 8
            it >= 2.3 && it < 3.0 -> 10
            it >= 3.0 && it < 4.0 -> 11
            it >= 4.0 && it < 4.1 -> 14
            it >= 4.1 && it < 4.4 -> 16
            it >= 4.4 && it < 5.0 -> 19
            it >= 5.0 && it < 5.1 -> 21
            it >= 6.0 && it < 7.0 -> 23
            it >= 7.0 && it < 7.1 -> 24
            it >= 8.0 && it < 8.1 -> 26
            it >= 9.0 && it < 10.0 -> 28
            it >= 10.0 && it < 11.0 -> 29
            it >= 11.0 && it < 12.0 -> 31
            it >= 13.0 && it < 14.0 -> 33
            it >= 14 -> 34
            else -> 1
        }
    } ?: 1
}


fun installsToNumber(installs: String): Int {
    val numberPattern = Regex("\\d+")
    val numberString = numberPattern.findAll(installs).joinToString("") { it.value }
    return numberString.toIntOrNull() ?: 0
}

fun priceToBoolean(price: String): Boolean {
    return price.toFloatOrNull() == 0.0f
}

fun convertDateToISO8601(dateString: String): String {
    val inputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}
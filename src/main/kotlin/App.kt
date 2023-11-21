import java.text.SimpleDateFormat
import java.util.*

class App(
    val name: String,
    val category: String,
    val rating: Double,
    val reviews: Int,
    val size: String,
    val installs: Int,
    val type: String,
    val price: Boolean,
    val contentRating: String,
    val genres: String,
    val lastUpdated: String,
    val currentVer: Int,
    val androidVer: Int
) {
    fun toJson(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "category" to category,
            "rating" to rating,
            "reviews" to reviews,
            "size" to size,
            "installs" to installs,
            "type" to type,
            "price" to price,
            "contentRating" to contentRating,
            "genres" to genres,
            "lastUpdated" to lastUpdated,
            "currentVer" to currentVer,
            "androidVer" to androidVer
        )
    }

    companion object {

        fun fromCsvLine(line: String): App {
            val columns = line.parseCsvLine()
            return App(
                name = columns[0],
                category = categoryTranslation[columns[1]] ?: "",
                rating = columns[2].toDoubleOrNull() ?: 0.0,
                reviews = columns[3].toIntOrNull() ?: 0,
                size = columns[4],
                installs = columns[5].installsToNumber(),
                type = columns[6],
                price = columns[7].priceToBoolean(),
                contentRating = columns[8],
                genres = columns[9],
                lastUpdated = columns[10].convertDateToISO8601(),
                currentVer = columns[11].androidVersionToApi(),
                androidVer = columns[12].androidVersionToApi()
            )
        }


    }
}

fun String.parseCsvLine(): List<String> {
    val result = mutableListOf<String>()
    var currentColumn = StringBuilder()
    var insideQuotes = false

    for (char in this) {
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

fun String.androidVersionToApi(): Int {
    val versionRegex = """(\d+\.\d+)""".toRegex()
    val matchResult = versionRegex.find(this)

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

fun String.installsToNumber(): Int {
    val numberPattern = Regex("\\d+")
    val numberString = numberPattern.findAll(this).joinToString("") { it.value }
    return numberString.toIntOrNull() ?: 0
}

fun String.priceToBoolean(): Boolean {
    return this.toFloatOrNull() == 0.0f
}

fun String.convertDateToISO8601(): String {
    val inputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}

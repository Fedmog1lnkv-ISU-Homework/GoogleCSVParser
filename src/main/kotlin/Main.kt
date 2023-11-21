import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val categoryTranslation = mapOf(
    "ART_AND_DESIGN" to "ИСКУССТВО И ДИЗАЙН",
    "AUTO_AND_VEHICLES" to "АВТОМОБИЛИ И ТРАНСПОРТ",
    "BEAUTY" to "КРАСОТА",
    "BOOKS_AND_REFERENCE" to "КНИГИ",
    "BUSINESS" to "БИЗНЕС",
    "COMICS" to "КОМИКСЫ",
    "COMMUNICATION" to "ОБЩЕНИЕ",
    "DATING" to "ЗНАКОМСТВА",
    "EDUCATION" to "ОБРАЗОВАНИЕ",
    "ENTERTAINMENT" to "РАЗВЛЕЧЕНИЯ",
    "EVENTS" to "СОБЫТИЯ",
    "FINANCE" to "ФИНАНСЫ",
    "FOOD_AND_DRINK" to "ЕДА И НАПИТКИ",
    "HEALTH_AND_FITNESS" to "ЗДОРОВЬЕ И ФИТНЕС",
    "HOUSE_AND_HOME" to "ДОМ",
    "LIBRARIES_AND_DEMO" to "БИБЛИОТЕКИ",
    "LIFESTYLE" to "ОБРАЗ ЖИЗНИ",
    "GAME" to "ИГРЫ",
    "FAMILY" to "СЕМЬЯ",
    "MEDICAL" to "МЕДИЦИНА",
    "SOCIAL" to "СОЦИАЛЬНЫЕ",
    "SHOPPING" to "ШОППИНГ",
    "PHOTOGRAPHY" to "ФОТОГРАФИЯ",
    "SPORTS" to "СПОРТ",
    "TRAVEL_AND_LOCAL" to "ПУТЕШЕСТВИЯ",
    "TOOLS" to "ИНСТРУМЕНТЫ",
    "PERSONALIZATION" to "ПЕРСОНАЛИЗАЦИЯ",
    "PRODUCTIVITY" to "ПРОДУКТИВНОСТЬ",
    "PARENTING" to "ВОСПИТАНИЕ ДЕТЕЙ",
    "WEATHER" to "ПОГОДА",
    "VIDEO_PLAYERS" to "ВИДЕОПЛЕЕРЫ",
    "NEWS_AND_MAGAZINES" to "НОВОСТИ И ЖУРНАЛЫ",
    "MAPS_AND_NAVIGATION" to "КАРТЫ И НАВИГАЦИЯ"
)

fun main() {

    val filePath = "src/main/resources/googleplaystore.csv"

    val apps = File(filePath).readLines().drop(1).map { line ->
        App.fromCsvLine(line)
    }

    val groupedData = apps.groupBy { it.category }

    val jsonResult = groupedData.mapValues { (_, apps) ->
        apps.map { app -> app.toJson() }
    }

    File("src/main/resources/out.json").writeText(jsonResult.toString())
}

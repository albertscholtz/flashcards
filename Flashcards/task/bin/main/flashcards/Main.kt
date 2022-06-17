package flashcards

import java.io.File

object Game {
    private val cards: MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
    private val errors: MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()
    val logger = StringBuilder()

    fun addCard(term: String, definition: String, error: Int) {
        cards[term] = definition
        errors[term] = error
    }

    fun removeCard(term: String): String? {
        errors.remove(term)
        return cards.remove(term)
    }

    fun getLog(): String {
        return logger.toString()
    }

    fun termExists(term: String): Boolean {
        return cards.contains(term)
    }

    fun definitionExists(definition: String): Boolean {
        return cards.containsValue(definition)
    }

    fun exportCards(): String {
        return cards.map { (term, definition) -> "$term~$definition~${errorForTerm(term)}" }.joinToString("\n")
    }

    fun countCards(): Int {
        return cards.size
    }

    fun terms(): List<String> {
        return cards.keys.toList()
    }

    fun definitionForTerm(term: String): String? {
        return cards[term]
    }

    fun termForDefinition(definition: String): String {
        return cards.filterValues { it == definition }.keys.iterator().next()
    }

    fun recordWrongAnswer(term: String) {
        val value = errors.getValue(term)
        errors[term] = value.inc()
    }

    fun hardestCards(): Pair<List<String>, Int> {
        if (errors.isEmpty()) return Pair(emptyList(), 0)

        val max = errors.values.maxOf { it }
        if (max == 0) return Pair(emptyList(), 0)

        val errorTerms = errors.filterValues { it == max }
        return Pair(errorTerms.keys.toList(), max)
    }

    fun resetStats() {
        for (key in errors.keys) {
            errors[key] = 0
        }
    }

    fun errorForTerm(term: String): Int {
        return errors.getValue(term)
    }
}

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        val importArg = "-import"
        if (args.contains(importArg)) {
            val indexOf = args.indexOf(importArg)
            val fileName = args[indexOf + 1]
            importCards(fileName)
        }
    }

    do {
        val action = inputFromMessage("Input the action (import, export, add, remove, ask, exit, log, hardest card, reset stats):")
        processAction(action)
    } while (action != "exit")

    if (args.isNotEmpty()) {
        val exportArg = "-export"
        if (args.contains(exportArg)) {
            val indexOf = args.indexOf(exportArg)
            val fileName = args[indexOf + 1]
            exportCards(fileName)
        }
    }
}

fun processAction(action: String) {
    when (action) {
        "add" -> addCard()
        "remove" -> removeCard()
        "import" -> importCardsWithPrompt()
        "export" -> exportCardsWithPrompt()
        "ask" -> ask()
        "exit" -> logAndPrint("Bye bye!")
        "log" -> saveLog()
        "hardest card" -> hardestCard()
        "reset stats" -> resetStats()
        else -> logAndPrint("Invalid action, please try again")
    }
}

fun addCard() {
    val term = inputFromMessage("The card:")
    if (Game.termExists(term)) {
        logAndPrint("The card \"$term\" already exists.")
    } else {
        val definition = inputFromMessage("The definition of the card:")
        if (Game.definitionExists(definition)) {
            logAndPrint("The definition \"$definition\" already exists.")
        } else {
            Game.addCard(term, definition, 0)
            logAndPrint("The pair (\"$term\":\"$definition\") has been added.")
        }
    }
}

fun removeCard() {
    val term = inputFromMessage("Which card?")
    val previousValue = Game.removeCard(term)
    if (!previousValue.isNullOrEmpty()) {
        logAndPrint("The card has been removed.")
    } else {
        logAndPrint("Can't remove \"$term\": there is no such card.")
    }
}

fun importCardsWithPrompt() {
    val fileName = inputFromMessage("File name:")
    importCards(fileName)
}

private fun importCards(fileName: String) {
    val file = File(fileName)
    if (!file.exists()) {
        logAndPrint("File not found.")
        return
    }

    val lines = file.readLines()
    lines.forEach { processImportLine(it) }
    logAndPrint("${lines.size} cards have been loaded.")
}

fun exportCardsWithPrompt() {
    val fileName = inputFromMessage("File name:")
    exportCards(fileName)
}

private fun exportCards(fileName: String) {
    val file = File(fileName)
    val text = Game.exportCards()
    file.writeText(text)
    logAndPrint("${Game.countCards()} cards have been saved.")
}

fun ask() {
    val times = inputFromMessage("How many times to ask?").toInt()
    val keys = Game.terms()
    repeat(times) {
        val randomIndex = (0 until Game.countCards()).random()
        val term = keys[randomIndex]
        val definition = Game.definitionForTerm(term)
        val answer = inputFromMessage("Print the definition of \"$term\"")
        if (answer == definition) {
            printCorrectAnswerMessage()
        } else definition?.let {
            Game.recordWrongAnswer(term)
            printWrongAnswerMessage(
                answer,
                it
            )
        }
    }
}

fun saveLog() {
    val fileName = inputFromMessage("File name:")
    val file = File(fileName)
    val log = Game.getLog()
    file.writeText(log)
    logAndPrint("The log has been saved.")
}

fun hardestCard() {
    val (terms, errors) = Game.hardestCards()
    if (terms.size > 1) {
        val errorTerms = terms.joinToString(", ") { "\"$it\"" }
        logAndPrint("The hardest cards are $errorTerms. You have $errors errors answering them.")
    } else if (terms.size == 1) {
        logAndPrint("The hardest card is \"${terms[0]}\". You have $errors errors answering it.")
    } else {
        logAndPrint("There are no cards with errors.")
    }
}

fun resetStats() {
    Game.resetStats()
    logAndPrint("Card statistics have been reset.")
}

fun processImportLine(line: String) {
    if (line.isBlank()) return

    val (term, definition,error) = line.split("~")
    Game.addCard(term, definition, error.toInt())
}

private fun inputFromMessage(message: String): String {
    logAndPrint(message)
    val input = readln()
    log(input)
    return input
}

fun printCorrectAnswerMessage() {
    logAndPrint("Correct!")
}

fun printWrongAnswerMessage(answer: String, rightAnswer: String) {
    if (Game.definitionExists(answer)) {
        val existingKey = Game.termForDefinition(answer)
        logAndPrint("Wrong. The right answer is \"$rightAnswer\", but your definition is correct for \"$existingKey\".")
    } else {
        logAndPrint("Wrong. The right answer is \"$rightAnswer\"")
    }
}

fun logAndPrint(text: String) {
    log(text)
    println(text)
}

fun log(text:String) {
    Game.logger.append("$text\n")
}

fun futureFunction(text: String) {
    log("The future function was called")
}
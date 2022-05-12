package flashcards

object Game {
    private val cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun getCardAtPosition(number: Int): Card {
        return cards[number - 1]
    }

    fun amountOfCardsInGame(): Int {
        return cards.size
    }
}

class Card(val term: String, val definition: String) {

    fun printCard() {
        println("Card:")
        println(term)
        println("Definition:")
        println(definition)
    }

    fun correctAnswer(answer: String): Boolean {
        return answer == definition
    }
}

fun main() {
    val numberOfCards = askForNumberOfCards()

    for (i in 1..numberOfCards) {
        val card = createCardFromInput(i)
        Game.addCard(card)
    }

    for (i in 1..Game.amountOfCardsInGame()) {
        val card = Game.getCardAtPosition(i)
        val answer = askAndCompareAnswer(card)
        if (card.correctAnswer(answer)) printCorrectAnswerMessage() else printWrongAnswerMessage(card)
    }
}

fun askForNumberOfCards(): Int {
    println("Input the number of cards: ")
    return readln().toInt()
}

fun createCardFromInput(i: Int): Card {
    println("Card #$i: ")
    val term = readln()
    println("The definition for card #$i: ")
    val definition = readln()
    return Card(term, definition)
}

fun askAndCompareAnswer(card: Card): String {
    println("Print the definition of \"${card.term}\"")
    return readln()
}

fun printWrongAnswerMessage(card: Card) {
    println("Wrong. The right answer is \"${card.definition}\"")
}

fun printCorrectAnswerMessage() {
    println("Correct!")
}

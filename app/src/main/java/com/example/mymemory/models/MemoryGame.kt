package com.example.mymemory.models

import com.example.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?){

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var NumCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null
    init{
        if(customImages == null){
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages+chosenImages).shuffled()
        cards = randomizedImages.map{MemoryCard(it)}
        }else{
            val randomizedImages = (customImages+customImages).shuffled()
            cards = randomizedImages.map {MemoryCard(it.hashCode(),it)}
        }
    }
    fun flipCard(position: Int):Boolean {
        NumCardFlips++
        val card = cards[position]
        // three cases:
        // 0 cards flipped => flip over the selected card
        // 1 cards flipped => flip over the selected card + check if the images match
        // 2 cards flipped => restore cards + flip over the selected card
        // case 0 and 2 identical
        var foundMatch = false
        if(indexOfSingleSelectedCard==null){
            // 0 or 2 cards prieviously flippped over
            restoreCards()
            indexOfSingleSelectedCard = position
        } else{
            // 1 card flipped over
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier!=cards[position2].identifier){
            return false
        }
        cards[position1].isMatched=true
        cards[position2].isMatched=true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for(card in cards){
            if(!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean{
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int{
        return NumCardFlips/2
    }
}
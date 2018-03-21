package com.msc.mastermind

class GameState(val guessAttempts: Int) {
    var colorCode: ColorCode? = null
    val guesses: MutableList<ColorCode> = mutableListOf()
    var nextDrawType = DrawType.SET_CODE
}

enum class DrawType {
    SET_CODE,
    TAKE_GUESS,
    RESPONSE_TO_GUESS
}
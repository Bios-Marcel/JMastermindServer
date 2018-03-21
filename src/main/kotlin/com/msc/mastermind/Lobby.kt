package com.msc.mastermind

import java.util.*

class Lobby(val inviteOnly: Boolean, val remoteAddress: String, guessAttempts: Int, automatedGuessResponses: Boolean) {

    val uuid = UUID.randomUUID().toString()
    val userSessionPlayerOne = UUID.randomUUID().toString()
    var userSessionPlayerTwo: Optional<String> = Optional.empty()

    val settings = Settings(guessAttempts, automatedGuessResponses)
    val gameState = GameState(guessAttempts)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(other is Lobby) {
            return uuid != other.uuid
        }

        return false
    }

    override fun hashCode() = uuid.hashCode()

    override fun toString() = uuid
}

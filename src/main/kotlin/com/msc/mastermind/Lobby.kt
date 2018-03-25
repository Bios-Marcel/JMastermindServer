package com.msc.mastermind

import com.msc.mastermind.entities.Player
import java.util.*

class Lobby(var owner: Player, val lobbySize: Int, val inviteOnly: Boolean, guessAttempts: Int, automatedGuessResponses: Boolean) {

    val uuid = UUID.randomUUID().toString()
    val players: MutableList<Player> = mutableListOf()

    val settings = Settings(guessAttempts, automatedGuessResponses)
    val gameState = GameState(guessAttempts)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Lobby) {
            return uuid != other.uuid
        }

        return false
    }

    override fun hashCode() = uuid.hashCode()

    override fun toString() = uuid
}

package com.msc.mastermind.api

import com.msc.mastermind.ConnectionInformation
import com.msc.mastermind.Lobby
import org.springframework.http.ResponseEntity
import java.util.*
import javax.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
object LobbyController {

    private val lobbies: MutableList<Lobby> = mutableListOf()

    @GetMapping("/lobby/create")
    fun createLobby(@RequestParam(value = "guessAttempts", defaultValue = "6") guessAttempts: Int,
                    @RequestParam(value = "inviteOnly", defaultValue = "true") inviteOnly: Boolean,
                    @RequestParam(value = "automatedGuessResponses", defaultValue = "true") automatedGuessResponses: Boolean,
                    response: HttpServletRequest): ResponseEntity<Any> {
        return createLobby(response.remoteAddr, guessAttempts, automatedGuessResponses, inviteOnly)
    }

    fun createLobby(remoteAddress: String, guessAttempts: Int = 6, inviteOnly: Boolean = true, automatedGuessResponses: Boolean = true): ResponseEntity<Any> {
        val lobby = Lobby(inviteOnly, remoteAddress, guessAttempts, automatedGuessResponses)
        val canCreateLobby = canCreateLobby(remoteAddress, lobby)

        if (canCreateLobby.isNullOrEmpty().not()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(canCreateLobby)
        }

        lobbies.add(lobby)
        return ResponseEntity.ok(ConnectionInformation(lobby.uuid, lobby.userSessionPlayerOne))
    }

    private fun canCreateLobby(ipAddress: String, lobby: Lobby): String? {
        if (lobbies.stream().anyMatch { it.remoteAddress == ipAddress }) {
            return "You have already created a lobby."
        }

        if (lobbies.stream().anyMatch { it.uuid == lobby.uuid }) {
            return "There is already a lobby with the previously generated UUID."
        }

        return null
    }

    @GetMapping("/lobby/join")
    fun joinLobby(@RequestParam(value = "uuid") uuid: String): ResponseEntity<Any> {
        val lobby = lobbies.find { it.uuid == uuid } ?: return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("The lobby you are trying to join doesn't exist.")

        if (lobby.userSessionPlayerTwo.isPresent) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("This lobby is already full")
        }

        val userSessionPlayerTwo = UUID.randomUUID().toString()
        lobby.userSessionPlayerTwo = Optional.of(userSessionPlayerTwo)

        return ResponseEntity.ok(ConnectionInformation(lobby.uuid, lobby.userSessionPlayerOne))
    }

    @GetMapping("/lobby/close")
    fun closeLobby(@RequestParam(value = "uuid") uuid: String, @RequestHeader(value = "userSession") userSession: String): ResponseEntity<Any> {
        val lobby = lobbies.find { it.uuid == uuid } ?: return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("The lobby you are trying to close doesn't exist.")

        if (lobby.userSessionPlayerOne != userSession) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The lobby can't be closed, due to invalid permissions.")
        }

        closeLobby(lobby)

        return ResponseEntity.ok("Lobby has been closed successfully")
    }

    private fun closeLobby(lobby: Lobby) {
        lobbies.remove(lobby)
        //TODO Implement actual close logic to notify second player
    }

    fun findGameState(userSession: String) = lobbies.find { it.userSessionPlayerOne == userSession }?.gameState

    fun closeAllLobbies() {
        lobbies.clear()
    }
}
package com.msc.mastermind.api

import com.msc.mastermind.*
import com.msc.mastermind.exceptions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LobbyController() : BaseController() {

    @Autowired
    lateinit var authenticationController: AuthenticationController

    constructor(authenticationController: AuthenticationController) : this() {
        this.authenticationController = authenticationController
    }

    private val lobbies: MutableList<Lobby> = mutableListOf()

    @GetMapping("/lobby/create")
    fun createLobby(@RequestParam(value = "guessAttempts", defaultValue = "6") guessAttempts: Int,
                    @RequestParam(value = "lobbySize", defaultValue = "2") lobbySize: Int,
                    @RequestParam(value = "inviteOnly", defaultValue = "true") inviteOnly: Boolean,
                    @RequestParam(value = "automatedGuessResponses", defaultValue = "true") automatedGuessResponses: Boolean,
                    @RequestHeader(value = "userSession") userSession: String): ResponseEntity<Response> {
        return createLobby(userSession, lobbySize, guessAttempts, automatedGuessResponses, inviteOnly)
    }

    fun createLobby(userSession: String, lobbySize: Int = 2, guessAttempts: Int = 6, inviteOnly: Boolean = true, automatedGuessResponses: Boolean = true): ResponseEntity<Response> {
        val player = authenticationController.findPlayer(userSession) ?: throw InvalidUserSessionException()
        canCreateLobby(userSession, lobbySize, guessAttempts)
        val lobby = Lobby(player, lobbySize, inviteOnly, guessAttempts, automatedGuessResponses)

        lobby.players.add(player)
        lobbies.add(lobby)
        return ResponseEntity.ok(Response(ResponseType.CONNECTION_INFORMATION, ConnectionInformation(lobby.uuid)))
    }

    private fun canCreateLobby(userSession: String, lobbySize: Int, guessAttempts: Int) {
        if (lobbies.stream().anyMatch { it.players.find { it.userSession == userSession } != null }) {
            throw CantCreateLobbyException("You have already created a lobby.")
        }

        if (guessAttempts < 1) {
            throw CantCreateLobbyException("A lobby needs to have at least 1 guess attempt.")
        }

        if (lobbySize < 2) {
            throw CantCreateLobbyException("The lobby needs to have space for at least 2 players.")
        }

        if ((lobbySize - 1) > guessAttempts) {
            throw CantCreateLobbyException("The lobby shouldn't have space for more codebreakers than there are guess attempts.")
        }
    }

    @GetMapping("/lobby/join")
    fun joinLobby(@RequestParam(value = "uuid") uuid: String, @RequestHeader(value = "userSession") userSession: String): ResponseEntity<Response> {
        val lobby = lobbies.find { it.uuid == uuid } ?: throw LobbyNotExistentException()

        if (lobby.players.size >= lobby.lobbySize) {
            throw LobbyFullException()
        }

        val player = authenticationController.findPlayer(userSession) ?: throw InvalidUserSessionException()

        lobby.players.add(player)

        return ResponseEntity.ok(Response(ResponseType.CONNECTION_INFORMATION, ConnectionInformation(lobby.uuid)))
    }

    @GetMapping("/lobby/close")
    fun closeLobby(@RequestHeader(value = "userSession") userSession: String): ResponseEntity<Response> {
        val player = authenticationController.findPlayer(userSession) ?: throw InvalidUserSessionException()
        val lobby = lobbies.find { it.players.find { it.userSession == userSession } != null }
                ?: throw LobbyNotExistentException("You aren't part of any lobby.")

        if (lobby.owner != player) {
            throw InsufficientPermissionsException("You are not the lobbies owner.")
        }

        closeLobby(lobby)

        return ResponseEntity.ok(Response(ResponseType.SUCCESS, Success("Lobby has been closed successfully")))
    }

    private fun closeLobby(lobby: Lobby) {
        lobbies.remove(lobby)
        //TODO Implement actual close logic to notify second player
    }

    fun findLobbyByUserSession(userSession: String) = lobbies.find { it.players.find { it.userSession == userSession } != null }
    fun findLobbyByUUID(lobbyUUID: String) = lobbies.find { it.uuid == lobbyUUID }
}
package com.msc.mastermind.api

import com.msc.mastermind.*
import com.msc.mastermind.exceptions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RoundController() : BaseController() {

    @Autowired
    lateinit var authenticationController: AuthenticationController

    @Autowired
    lateinit var lobbyController: LobbyController

    constructor(lobbyController: LobbyController, authenticationController: AuthenticationController) : this() {
        this.lobbyController = lobbyController
        this.authenticationController = authenticationController
    }

    @GetMapping("/round/setcode")
    fun setCode(@RequestBody colorCode: ColorCode, @RequestHeader(value = "userSession") userSession: String): ResponseEntity<Response> {
        val player = authenticationController.findPlayer(userSession) ?: throw InvalidUserSessionException()
        val lobby = lobbyController.findLobbyByUserSession(userSession) ?: throw LobbyNotExistentException()

        if (lobby.gameState.colorCode != null) {
            throw ColorCodeAlreadySpecifiedException()
        }

        lobby.gameState.colorCode = colorCode

        return ResponseEntity.ok(Response(ResponseType.SUCCESS, Success("Successfully specified the color code.")))
    }

    fun takeGuess(@RequestBody colorCode: ColorCode, @RequestHeader(value = "userSession") userSession: String): ResponseEntity<Response> {
        val lobby = lobbyController.findLobbyByUserSession(userSession) ?: throw LobbyNotExistentException()

        return when (lobby.gameState.nextDrawType) {
            DrawType.SET_CODE -> throw NoColorCodeSpecifiedException()
            DrawType.RESPONSE_TO_GUESS -> throw NoResponseToLastGuessException()
            DrawType.TAKE_GUESS -> {
                lobby.gameState.guesses.add(colorCode)
                return ResponseEntity.ok(Response(ResponseType.SUCCESS, Success("You successfully took a guess.")))
            }
        }
    }
}

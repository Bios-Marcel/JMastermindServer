package com.msc.mastermind.api

import com.msc.mastermind.ColorCode
import com.msc.mastermind.Success
import com.msc.mastermind.entities.Player
import com.msc.mastermind.exceptions.ColorCodeAlreadySpecifiedException
import com.msc.mastermind.exceptions.InvalidUserSessionException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RoundControllerTest {

    private val authenticationController = AuthenticationController()
    private val player = authenticationController.login("Test").body.data as Player

    @Test
    fun testSetCode() {
        with(RoundController(LobbyController(authenticationController), authenticationController)) {
            lobbyController.createLobby(player.userSession)
            val colorCode = ColorCode(1, 2, 3, 4, 5)

            Assertions.assertTrue(setCode(colorCode, player.userSession).body.data is Success)
            Assertions.assertThrows(InvalidUserSessionException::class.java) {
                setCode(colorCode, "Incorrect User Session")
            }

            Assertions.assertThrows(ColorCodeAlreadySpecifiedException::class.java) {
                setCode(colorCode, player.userSession)
            }
        }
    }

    //TODO testTakeGuess
}
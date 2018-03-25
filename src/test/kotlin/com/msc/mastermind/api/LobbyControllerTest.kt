package com.msc.mastermind.api

import com.msc.mastermind.ConnectionInformation
import com.msc.mastermind.Success
import com.msc.mastermind.entities.Player
import com.msc.mastermind.exceptions.CantCreateLobbyException
import com.msc.mastermind.exceptions.InvalidUserSessionException
import com.msc.mastermind.exceptions.LobbyNotExistentException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LobbyControllerTest {

    private val authenticationController = AuthenticationController()
    private val player = authenticationController.login("Test").body.data as Player

    @Test
    fun testCreate() {
        with(LobbyController(authenticationController)) {
            Assertions.assertThrows(InvalidUserSessionException::class.java) {
                createLobby("Invalid User Session")
            }
            Assertions.assertThrows(CantCreateLobbyException::class.java) {
                createLobby(player.userSession, guessAttempts = 0)
            }
            Assertions.assertThrows(CantCreateLobbyException::class.java) {
                createLobby(player.userSession, lobbySize = 1)
            }
            Assertions.assertThrows(CantCreateLobbyException::class.java) {
                createLobby(player.userSession, lobbySize = 3, guessAttempts = 1)
            }

            Assertions.assertEquals(ConnectionInformation::class, createLobby(player.userSession).body.data::class)

            Assertions.assertThrows(CantCreateLobbyException::class.java) {
                createLobby(player.userSession)
            }
        }
        //Testing
    }

    @Test
    fun testClose() {
        with(LobbyController(authenticationController)) {
            Assertions.assertThrows(LobbyNotExistentException::class.java) {
                closeLobby(player.userSession)
            }

            val connectionInformation = createLobby(player.userSession).body.data as ConnectionInformation

            Assertions.assertThrows(InvalidUserSessionException::class.java) {
                closeLobby("Invalid UserSession")
            }

            Assertions.assertEquals(Success::class, closeLobby(player.userSession).body.data::class)
        }
    }
}
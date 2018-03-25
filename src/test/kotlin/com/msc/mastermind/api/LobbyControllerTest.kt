package com.msc.mastermind.api

import com.msc.mastermind.Success
import com.msc.mastermind.entities.Player
import com.msc.mastermind.exceptions.CantCreateLobbyException
import com.msc.mastermind.exceptions.InvalidUserSessionException
import com.msc.mastermind.exceptions.LobbyNotExistentException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class LobbyControllerTest {

    @Test
    fun testCreate(@Autowired lobbyController: LobbyController, @Autowired authenticationController: AuthenticationController) {
        val player = authenticationController.login("Test1").body.data as Player

        with(lobbyController) {
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

            Assertions.assertEquals(String::class, createLobby(player.userSession).body.data::class)

            Assertions.assertThrows(CantCreateLobbyException::class.java) {
                createLobby(player.userSession)
            }
        }
    }

    @Test
    fun testClose(@Autowired lobbyController: LobbyController, @Autowired authenticationController: AuthenticationController) {
        val player = authenticationController.login("Test2").body.data as Player

        with(lobbyController) {
            Assertions.assertThrows(LobbyNotExistentException::class.java) {
                closeLobby(player.userSession)
            }

            createLobby(player.userSession)

            Assertions.assertThrows(InvalidUserSessionException::class.java) {
                closeLobby("Invalid UserSession")
            }

            Assertions.assertEquals(Success::class, closeLobby(player.userSession).body.data::class)
        }
    }
}
package com.msc.mastermind.api

import com.msc.mastermind.ConnectionInformation
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class LobbyControllerTest {

    @Test
    fun testCreate() {
        val ipAddress = "127.0.0.1"

        //Testing
        Assertions.assertEquals(HttpStatus.OK, LobbyController.createLobby(ipAddress).statusCode)
        Assertions.assertEquals(HttpStatus.OK, LobbyController.createLobby(ipAddress + "2").statusCode)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, LobbyController.createLobby(ipAddress).statusCode)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, LobbyController.createLobby(ipAddress).statusCode)
    }

    @Test
    fun testDelete() {
        val ipAddress = "127.0.0.1"

        val connectionInformation = LobbyController.createLobby(ipAddress).body as ConnectionInformation
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, LobbyController.closeLobby(connectionInformation.lobbyUUID, "Invalid UserSession").statusCode)
        Assertions.assertEquals(HttpStatus.OK, LobbyController.closeLobby(connectionInformation.lobbyUUID, connectionInformation.sessionUUID).statusCode)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, LobbyController.closeLobby(connectionInformation.lobbyUUID, connectionInformation.sessionUUID).statusCode)
        Assertions.assertEquals(HttpStatus.OK, LobbyController.createLobby(ipAddress).statusCode)
    }

    @AfterEach
    fun cleanup() {
        LobbyController.closeAllLobbies()
    }
}
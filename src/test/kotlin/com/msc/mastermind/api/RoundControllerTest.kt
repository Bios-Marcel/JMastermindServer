package com.msc.mastermind.api

import com.msc.mastermind.ColorCode
import com.msc.mastermind.ConnectionInformation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class RoundControllerTest {
    private val connectionInformation = LobbyController.createLobby("127.0.0.1").body as ConnectionInformation

    @Test
    fun testSetCode() {
        val colorCode = ColorCode(1, 2, 3, 4, 5)
        Assertions.assertEquals(HttpStatus.OK, RoundController.setCode(colorCode, connectionInformation.sessionUUID).statusCode)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, RoundController.setCode(colorCode, "Incorrect UUID").statusCode)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, RoundController.setCode(colorCode, connectionInformation.sessionUUID).statusCode)
    }

//    @Test
//    fun testTakeGuess() {
//    }
}
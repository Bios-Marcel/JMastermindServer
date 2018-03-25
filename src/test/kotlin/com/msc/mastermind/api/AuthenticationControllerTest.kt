package com.msc.mastermind.api

import com.msc.mastermind.entities.Player
import com.msc.mastermind.exceptions.NameAlreadyInUseException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AuthenticationControllerTest {
    @Test
    fun testLogin() {
        val authenticationController = AuthenticationController()
        val name = "TestName"
        Assertions.assertEquals(name, (authenticationController.login(name).body.data as Player).name)
        Assertions.assertThrows(NameAlreadyInUseException::class.java) {
            authenticationController.login(name)
        }
    }
}
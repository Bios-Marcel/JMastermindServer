package com.msc.mastermind.api

import com.msc.mastermind.ColorCode
import com.msc.mastermind.DrawType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
object RoundController {

    @GetMapping("/round/setcode")
    fun setCode(@RequestBody colorCode: ColorCode, @RequestHeader(value = "userSession") userSession: String): ResponseEntity<String> {
        val gameState = LobbyController.findGameState(userSession) ?: return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("No lobby where you are allowed to set the code has been found.")

        if (gameState.colorCode != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The code has already been set.")
        }

        gameState.colorCode = colorCode

        return ResponseEntity.ok().build()
    }

    fun takeGuess(@RequestBody colorCode: ColorCode, @RequestHeader(value = "userSession") userSession: String): ResponseEntity<String> {
        val gameState = LobbyController.findGameState(userSession) ?: return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("No lobby where you are allowed to take a guess has been found.")

        return when (gameState.nextDrawType) {
            DrawType.SET_CODE -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The codemaker hasn't specified a color code yet.")
            DrawType.RESPONSE_TO_GUESS -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The codemaker didn't respond to your last action yet.")
            DrawType.TAKE_GUESS -> {
                gameState.guesses.add(colorCode)
                return ResponseEntity.ok().build()
            }
        }
    }
}

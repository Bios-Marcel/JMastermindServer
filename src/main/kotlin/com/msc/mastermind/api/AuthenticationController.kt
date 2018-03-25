package com.msc.mastermind.api

import com.msc.mastermind.Response
import com.msc.mastermind.ResponseType
import com.msc.mastermind.entities.Player
import com.msc.mastermind.exceptions.NameAlreadyInUseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AuthenticationController {
    val players: MutableList<Player> = mutableListOf()

    @GetMapping("/auth/login")
    fun login(name: String): ResponseEntity<Response> {
        if (players.stream().anyMatch { it.name == name }) {
            throw NameAlreadyInUseException()
        }

        return ResponseEntity.ok(Response(ResponseType.PLAYER, createAndAddPlayer(name)))
    }

    private fun createAndAddPlayer(name: String): Player {
        val player = Player(name, UUID.randomUUID().toString())
        players.add(player)
        return player
    }

    fun findPlayer(userSession: String) = players.find { it.userSession == userSession }
}
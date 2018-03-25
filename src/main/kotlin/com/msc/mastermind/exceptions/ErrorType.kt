package com.msc.mastermind.exceptions

import com.fasterxml.jackson.annotation.JsonValue

enum class ErrorType(private val errorCode: Int) {
    NAME_ALREADY_IN_USE(100),
    INVALID_USER_SESSION(200),
    INSUFFICIENT_PERMISSIONS(300),
    CANT_CREATE_LOBBY(400),
    LOBBY_NOT_EXISTENT(401),
    LOBBY_FULL(402),
    NO_COLOR_CODE_SPECIFIED(500),
    COLOR_CODE_ALREADY_SPECIFIED(501),
    NO_RESPONSE_TO_LAST_GUESS(502);

    @JsonValue
    override fun toString() = errorCode.toString()
}

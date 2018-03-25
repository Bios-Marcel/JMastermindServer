package com.msc.mastermind.api

import com.msc.mastermind.Error
import com.msc.mastermind.Response
import com.msc.mastermind.ResponseType
import com.msc.mastermind.exceptions.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.util.WebUtils

open class BaseController {

    private fun RuntimeException.elseGet(alternativeMessage: String) = if (this.message.isNullOrEmpty()) alternativeMessage else this.message!!

    @ExceptionHandler
    fun handleNameAlreadyInUse(exception: NameAlreadyInUseException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("The name you have chosen is already in use.")
        return handleException(exception, ErrorType.NAME_ALREADY_IN_USE, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleInvalidUserSession(exception: InvalidUserSessionException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("Your user session is invalid, make sure you are logged in.")
        return handleException(exception, ErrorType.INVALID_USER_SESSION, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleInsufficientPermissions(exception: InsufficientPermissionsException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("Your permissions are insufficient.")
        return handleException(exception, ErrorType.INSUFFICIENT_PERMISSIONS, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleCantCreateLobby(exception: CantCreateLobbyException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("The lobby can't be created.")
        return handleException(exception, ErrorType.CANT_CREATE_LOBBY, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleLobbyNotExistent(exception: LobbyNotExistentException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("The lobby doesn't exist.")
        return handleException(exception, ErrorType.LOBBY_NOT_EXISTENT, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleLobbyFull(exception: LobbyFullException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("This lobby is already full")
        return handleException(exception, ErrorType.LOBBY_FULL, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleNoColorCodeSpecified(exception: NoColorCodeSpecifiedException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("The codemaker hasn't specified a color code yet.")
        return handleException(exception, ErrorType.NO_COLOR_CODE_SPECIFIED, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler
    fun handleColorCodeAlreadySpecified(exception: ColorCodeAlreadySpecifiedException, request: WebRequest): ResponseEntity<Response> {
        val message: String = exception.elseGet("The color code has already been specified.")
        return handleException(exception, ErrorType.COLOR_CODE_ALREADY_SPECIFIED, message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    private fun handleException(exception: Exception, errorType: ErrorType, message: String, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Response> {
        if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception, WebRequest.SCOPE_REQUEST)
        }
        return ResponseEntity(Response(ResponseType.ERROR, Error(errorType, message)), headers, status)
    }
}
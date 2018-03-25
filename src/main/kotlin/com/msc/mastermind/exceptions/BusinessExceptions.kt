package com.msc.mastermind.exceptions

class NameAlreadyInUseException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class InvalidUserSessionException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class CantCreateLobbyException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class LobbyNotExistentException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class LobbyFullException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class InsufficientPermissionsException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class NoColorCodeSpecifiedException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class ColorCodeAlreadySpecifiedException(message: String) : RuntimeException(message) {
    constructor() : this("")
}

class NoResponseToLastGuessException(message: String) : RuntimeException(message) {
    constructor() : this("")
}
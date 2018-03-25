package com.msc.mastermind

import com.msc.mastermind.exceptions.ErrorType

data class Response(val type: ResponseType, val data: Any)
data class Error(val id: ErrorType, val message: String)
data class Success(val message: String)
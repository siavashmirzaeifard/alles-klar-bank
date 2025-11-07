package co.alles_klar.bank.utility.exception_handler

import co.alles_klar.bank.utility.constant.ErrorTypes
import co.alles_klar.bank.utility.exception.AccountNotFoundException
import co.alles_klar.bank.utility.exception.InsufficientMoneyException
import co.alles_klar.bank.utility.exception.InvalidCredentialsException
import co.alles_klar.bank.utility.exception.InvalidPasswordException
import co.alles_klar.bank.utility.exception.InvalidTokenException
import co.alles_klar.bank.utility.exception.InvalidTransactionException
import co.alles_klar.bank.utility.exception.SamePasswordException
import co.alles_klar.bank.utility.exception.UserAlreadyExistsException
import co.alles_klar.bank.utility.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun onHttpMessageNotReadable(e: HttpMessageNotReadableException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "code" to ErrorTypes.BAD_REQUEST,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun onIllegalArgument(e: IllegalArgumentException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "code" to ErrorTypes.BAD_REQUEST,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun onIllegalArgument(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val error = e.bindingResult.allErrors.map { it.defaultMessage ?: "Invalid input argument" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "code" to ErrorTypes.BAD_REQUEST,
                    "message" to error
                )
            )
    }

    @ExceptionHandler(value = [InvalidTokenException::class])
    fun onInvalidToken(e: InvalidTokenException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                mapOf(
                    "code" to ErrorTypes.UNAUTHORIZED,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [InvalidPasswordException::class])
    fun onInvalidPassword(e: InvalidPasswordException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                mapOf(
                    "code" to ErrorTypes.UNAUTHORIZED,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [UserAlreadyExistsException::class])
    fun onUserAlreadyExists(e: UserAlreadyExistsException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                mapOf(
                    "code" to ErrorTypes.CONFLICT,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [InvalidCredentialsException::class])
    fun onInvalidCredentials(e: InvalidCredentialsException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                mapOf(
                    "code" to ErrorTypes.UNAUTHORIZED,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [UserNotFoundException::class])
    fun onUserNotFound(e: UserNotFoundException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                mapOf(
                    "code" to ErrorTypes.NOT_FOUND,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [SamePasswordException::class])
    fun onSamePassword(e: SamePasswordException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                mapOf(
                    "code" to ErrorTypes.CONFLICT,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [AccountNotFoundException::class])
    fun onAccountNotFound(e: AccountNotFoundException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                mapOf(
                    "code" to ErrorTypes.NOT_FOUND,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [InsufficientMoneyException::class])
    fun onInsufficientMoney(e: InsufficientMoneyException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                mapOf(
                    "code" to ErrorTypes.FORBIDDEN,
                    "message" to e.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [InvalidTransactionException::class])
    fun onInvalidTransaction(e: InvalidTransactionException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                mapOf(
                    "code" to ErrorTypes.FORBIDDEN,
                    "message" to e.localizedMessage
                )
            )
    }

}
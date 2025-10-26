package com.clarxlabs.lillia.application

import com.clarxlabs.lillia.application.extensions.errors
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import jakarta.validation.ConstraintViolationException
import reactor.core.publisher.Mono

@Controller
class FallbackController {
    @Error(exception = ConstraintViolationException::class, global = true)
    fun onValidationError(it: ConstraintViolationException): Mono<HttpResponse<Map<String, List<String>>>> =
        it.constraintViolations.errors()
            .let { HttpResponse.badRequest(it) }
            .let { Mono.just(it) }

    @Error(exception = IllegalArgumentException::class, global = true)
    fun onArgumentError(it: IllegalArgumentException): Mono<HttpResponse<Map<String, List<String>>>> =
        mapOf("errors" to listOf(it.message?.replaceFirstChar(Char::lowercase)!!))
            .let { HttpResponse.badRequest(it) }
            .let { Mono.just(it) }
}

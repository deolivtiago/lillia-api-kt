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
}

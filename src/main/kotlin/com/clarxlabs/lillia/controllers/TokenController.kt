package com.clarxlabs.lillia.controllers

import com.clarxlabs.lillia.application.extensions.filterSort
import com.clarxlabs.lillia.application.extensions.toPagedList
import com.clarxlabs.lillia.controllers.dtos.PagedList
import com.clarxlabs.lillia.entities.Token
import com.clarxlabs.lillia.repositories.TokenRepository
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.validation.Valid
import reactor.core.publisher.Mono
import java.util.*

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/tokens")
class TokenController(private val tokenRepository: TokenRepository) {

    @Get("/")
    @Status(HttpStatus.OK)
    fun index(@Valid input: Pageable): Mono<PagedList<Token>> =
        tokenRepository.findAll(input.filterSort(Token::isSortingProperty)).toPagedList()

    @Post("/")
    @Status(HttpStatus.CREATED)
    fun create(@Body @Valid input: Token): Mono<Token> =
        tokenRepository.save(input)

    @Get("/{id}")
    @Status(HttpStatus.OK)
    fun show(@Valid id: UUID): Mono<Token> =
        tokenRepository.findById(id)

    @Put("/{id}")
    @Status(HttpStatus.OK)
    fun update(@Valid id: UUID, @Body @Valid input: Token): Mono<Token> =
        input.copy(id = id).let { tokenRepository.update(it) }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@Valid id: UUID): Mono<Void> =
        tokenRepository.deleteById(id).then()
}

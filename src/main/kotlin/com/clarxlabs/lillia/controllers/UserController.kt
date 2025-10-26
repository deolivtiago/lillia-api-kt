package com.clarxlabs.lillia.controllers

import com.clarxlabs.lillia.application.extensions.filterSort
import com.clarxlabs.lillia.application.extensions.toPagedList
import com.clarxlabs.lillia.controllers.dtos.PagedList
import com.clarxlabs.lillia.entities.User
import com.clarxlabs.lillia.repositories.UserRepository
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
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/users")
class UserController(private val userRepository: UserRepository) {
    @Get("/")
    @Status(HttpStatus.OK)
    fun index(@Valid input: Pageable): Mono<PagedList<User>> =
        userRepository.findAll(input.filterSort(User::isSortingProperty)).toPagedList()

    @Post("/")
    @Status(HttpStatus.CREATED)
    fun create(@Body @Valid input: User): Mono<User> =
        userRepository.save(input)

    @Get("/{id}")
    @Status(HttpStatus.OK)
    fun show(@Valid id: UUID): Mono<User> =
        userRepository.findById(id)

    @Put("/{id}")
    @Status(HttpStatus.OK)
    fun update(@Valid id: UUID, @Body @Valid input: User): Mono<User> =
        userRepository.update(input.copy(id = id))

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@Valid id: UUID): Mono<Void> =
        userRepository.deleteById(id).then()
}

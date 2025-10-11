package com.clarxlabs.lillia.controllers

import com.clarxlabs.lillia.application.extensions.copy
import com.clarxlabs.lillia.application.extensions.filterProperties
import com.clarxlabs.lillia.application.extensions.toPagedList
import com.clarxlabs.lillia.controllers.dtos.PagedList
import com.clarxlabs.lillia.entities.Role
import com.clarxlabs.lillia.repositories.RoleRepository
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import reactor.core.publisher.Mono

@Validated
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/roles")
class RoleController(private val roleRepository: RoleRepository) {

    @Get("/")
    @Status(HttpStatus.OK)
    fun index(@Valid input: Pageable): Mono<PagedList<Role>> =
        input.copy(orderBy = input.orderBy.filterProperties(Role::isSortingProperty))
            .let { roleRepository.findAll(it).toPagedList() }

    @Post("/")
    @Status(HttpStatus.CREATED)
    fun create(@Body @Valid input: Role): Mono<Role> =
        roleRepository.save(input)

    @Get("/{id}")
    @Status(HttpStatus.OK)
    fun show(@NotBlank id: String): Mono<Role> =
        roleRepository.findById(id)

    @Put("/{id}")
    @Status(HttpStatus.OK)
    fun update(@NotBlank id: String, @Body @Valid input: Role): Mono<Role> =
        input.copy(id = id).let { roleRepository.update(it) }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@NotBlank id: String): Mono<Void> =
        roleRepository.deleteById(id).then()
}

package com.clarxlabs.lillia.repositories

import com.clarxlabs.lillia.entities.User
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import reactor.core.publisher.Mono
import java.util.*

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface UserRepository : ReactorPageableRepository<@Valid User, @Valid UUID> {
    fun findByUsername(username: @NotEmpty String): Mono<User>
}

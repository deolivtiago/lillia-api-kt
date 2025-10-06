package com.clarxlabs.lillia.repositories

import com.clarxlabs.lillia.entities.Role
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface RoleRepository : ReactorPageableRepository<@Valid Role, @NotBlank String> {
}

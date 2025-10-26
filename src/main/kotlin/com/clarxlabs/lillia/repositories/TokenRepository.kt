package com.clarxlabs.lillia.repositories

import com.clarxlabs.lillia.entities.Token
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import jakarta.validation.Valid
import java.util.*

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface TokenRepository : ReactorPageableRepository<@Valid Token, @Valid UUID> {
}

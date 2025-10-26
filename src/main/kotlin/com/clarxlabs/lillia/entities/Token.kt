package com.clarxlabs.lillia.entities

import io.micronaut.data.annotation.*
import io.micronaut.data.model.DataType
import io.micronaut.security.annotation.CreatedBy
import io.micronaut.security.annotation.UpdatedBy
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Where("@.is_revoked = false")
@Serdeable
@MappedEntity(value = "tokens", alias = "t")
data class Token(
    @field:Id
    @field:NotNull
    @field:Valid
    val id: UUID? = null,

    @field:NotBlank
    @field:Size(min = 3, max = 128)
    @field:Pattern(regexp = "^[\\w.\\-@]+$", message = "must have a valid format")
    val username: String = "",

    @field:TypeDef(type = DataType.STRING_ARRAY)
    @field:NotNull
    val roles: Set<@NotBlank @Size(min = 3, max = 64) @Pattern(
        regexp = "^([a-z0-9](_)?)+$",
        message = "must have a valid format"
    ) String> = emptySet(),

    val isRevoked: Boolean = false,

    @field:CreatedBy
    val createdBy: String? = "",

    @field:UpdatedBy
    val updatedBy: String? = "",

    @field:DateCreated(truncatedTo = ChronoUnit.MILLIS)
    val createdAt: ZonedDateTime? = null,

    @field:DateUpdated(truncatedTo = ChronoUnit.MILLIS)
    val updatedAt: ZonedDateTime? = null,
) {
    companion object {
        fun isSortingProperty(property: String): Boolean =
            setOf("username", "isRevoked", "updatedBy", "createdBy", "updatedAt", "createdAt")
                .contains(property)

        fun of(it: RefreshTokenGeneratedEvent): Token = Token(
            id = UUID.fromString(it.refreshToken),
            username = it.authentication.name,
            roles = it.authentication.roles.toSet(),
            createdBy = it.authentication.name,
            updatedBy = it.authentication.name,
        )
    }
}

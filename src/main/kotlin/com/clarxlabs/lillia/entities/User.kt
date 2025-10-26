package com.clarxlabs.lillia.entities

import io.micronaut.data.annotation.*
import io.micronaut.security.annotation.CreatedBy
import io.micronaut.security.annotation.UpdatedBy
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Serdeable
@MappedEntity(value = "users", alias = "u")
data class User(
    @field:Id
    @field:AutoPopulated
    val id: UUID? = null,

    @field:NotBlank
    @field:Size(min = 3, max = 128)
    @field:Pattern(regexp = "^[\\w.\\-@]+$", message = "must have a valid format")
    val username: String,

    @field:NotBlank
    @field:Size(min = 6, max = 128)
    val password: String,

    @field:NotBlank
    @field:Size(min = 2, max = 255)
    val fullName: String,

    @field:Pattern(regexp = "^http|ftp", message = "must have a valid format")
    val avatarUrl: String = "",

    val isVerified: Boolean = false,

    @field:NotBlank
    val roleId: String = "user",

    @field:CreatedBy
    val createdBy: String? = null,

    @field:UpdatedBy
    val updatedBy: String? = null,

    @field:DateCreated(truncatedTo = ChronoUnit.MILLIS)
    val createdAt: ZonedDateTime? = null,

    @field:DateUpdated(truncatedTo = ChronoUnit.MILLIS)
    val updatedAt: ZonedDateTime? = null,
) {
    companion object {
        fun isSortingProperty(property: String): Boolean =
            setOf("username", "fullName", "avatarUrl", "isVerified", "updatedBy", "createdBy", "updatedAt", "createdAt")
                .contains(property)
    }
}

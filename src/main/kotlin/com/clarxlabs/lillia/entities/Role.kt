package com.clarxlabs.lillia.entities

import io.micronaut.data.annotation.*
import io.micronaut.data.model.DataType
import io.micronaut.security.annotation.CreatedBy
import io.micronaut.security.annotation.UpdatedBy
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.validation.Validated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.ZonedDateTime

@Validated
@Serdeable
@MappedEntity(value = "roles", alias = "r")
data class Role(
    @field:Id
    @field:NotBlank
    @field:Size(min = 3, max = 64)
    @field:Pattern(regexp = "^([a-z0-9](_)?)+$", message = "must have a valid format")
    val id: String,

    @field:TypeDef(type = DataType.STRING_ARRAY)
    val permissions: Set<@NotBlank @Pattern(
        regexp = "^(GET|POST|PUT|DELETE):([a-z0-9][-/]?)+$",
        message = "must have a valid format"
    ) String> = emptySet(),

    @field:UpdatedBy
    val updatedBy: String? = null,

    @field:CreatedBy
    val createdBy: String? = null,

    @field:DateUpdated
    val updatedAt: ZonedDateTime? = null,

    @field:DateCreated
    val createdAt: ZonedDateTime? = null,
) {
    companion object {
        fun isSortingProperty(property: String): Boolean =
            setOf("id", "updatedBy", "createdBy", "updatedAt", "createdAt").contains(property)
    }
}

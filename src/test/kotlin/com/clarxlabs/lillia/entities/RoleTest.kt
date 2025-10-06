package com.clarxlabs.lillia.entities

import com.clarxlabs.lillia.application.extensions.errors
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

@MicronautTest(transactional = false)
class RoleTest {
    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Inject
    lateinit var validator: Validator

    @Test
    fun shouldFailWhenIdHasInvalidFormat() {
        val sut = Role("INVALID-F0RMAT!")
        val expected = mapOf("id" to listOf("must have a valid format"))

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldFailWhenIdIsTooShort() {
        val sut = Role("no")
        val expected = mapOf("id" to listOf("size must be between 3 and 64"))

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldFailWhenPermissionHasInvalidFormat() {
        val sut = Role("sorcerer_lvl666", setOf("INVALID:permission/"))
        val expected = mapOf("permissions" to listOf("must have a valid format"))

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldPassWhenRoleIsValid() {
        val sut = Role(
            id = "witcher",
            permissions = setOf("GET:roles/", "POST:roles"),
            updatedBy = "wizard",
            createdBy = "wizard",
            updatedAt = ZonedDateTime.now(),
            createdAt = ZonedDateTime.now()
        )

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(emptyMap<String, List<String>>(), actual)
    }
}

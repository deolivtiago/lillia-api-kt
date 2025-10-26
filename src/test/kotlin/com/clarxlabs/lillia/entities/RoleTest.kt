package com.clarxlabs.lillia.entities

import com.clarxlabs.lillia.application.extensions.errors
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

@MicronautTest(transactional = false)
class RoleTest {
    @Inject
    lateinit var validator: Validator

    @Test
    fun `should fail when the role id has an invalid format`() {
        val sut = Role("INVALID-F0RMAT!")
        val expected = mapOf("id" to listOf("must have a valid format"))

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should fail when the role id is too short`() {
        val sut = Role("no")
        val expected = mapOf("id" to listOf("size must be between 3 and 64"))

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should fail when the role permission has an invalid format`() {
        val sut = Role("sorcerer_lvl666", setOf("INVALID:permission/"))
        val expected = mapOf("permissions" to listOf("must have a valid format"))

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should pass when the role is valid`() {
        val sut = Role(
            id = "witcher",
            permissions = setOf("GET:roles/", "POST:roles"),
            createdBy = "wizard",
            updatedBy = "wizard",
            createdAt = ZonedDateTime.now(),
            updatedAt = ZonedDateTime.now(),
        )

        val actual = validator.validate(sut).errors()

        Assertions.assertEquals(emptyMap<String, List<String>>(), actual)
    }
}

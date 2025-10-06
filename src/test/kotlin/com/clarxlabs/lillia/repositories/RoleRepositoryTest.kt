package com.clarxlabs.lillia.repositories

import com.clarxlabs.lillia.entities.Role
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import kotlin.random.Random
import kotlin.random.nextUInt

@MicronautTest(transactional = false)
class RoleRepositoryTest {

    @Inject
    lateinit var roleRepository: RoleRepository

    @Test
    fun shouldSaveWhenRoleIsValid() {
        val sut = Role("witcher", setOf("GET:roles/", "POST:roles"), "wizard", "wizard")

        StepVerifier.create(roleRepository.save(sut))
            .assertNext { roleAssertions(sut, it) }
            .verifyComplete()
    }

    @Test
    fun shouldNotSaveWhenRoleIsInvalid() {
        val sut = Role("", setOf("INVALID-R0le!"))

//        reactive repositories with validation are not returning Mono.error() yet
//
//        StepVerifier.create(roleRepository.save(sut))
//            .expectError(ConstraintViolationException::class.java)
//            .verify()

        Assertions.assertThrows(ConstraintViolationException::class.java) { roleRepository.save(sut) }
    }

    @Test
    fun shouldFindByIdWhenRoleIdIsFound() {
        val sut = insertRole()

        StepVerifier.create(roleRepository.findById(sut.id))
            .assertNext { roleAssertions(sut, it) }
            .verifyComplete()
    }

    @Test
    fun shouldNotFindByIdWhenRoleIdIsNotFound() {
        StepVerifier.create(roleRepository.findById("not_found_id"))
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun shouldDeleteByIdWhenRoleIdIsFound() {
        val sut = insertRole()

        StepVerifier.create(roleRepository.deleteById(sut.id))
            .assertNext { Assertions.assertEquals(1, it) }
            .verifyComplete()

        StepVerifier.create(roleRepository.findById(sut.id))
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun shouldNotDeleteByIdWhenRoleIdIsNotFound() {
        StepVerifier.create(roleRepository.deleteById("not_found_id"))
            .assertNext { Assertions.assertEquals(0, it) }
            .verifyComplete()
    }

    private fun insertRole(): Role = roleRepository
        .save(Role("sorcerer_lvl" + Random.nextUInt(), emptySet(), "wizard", "wizard")).block()!!

    private fun roleAssertions(expected: Role, actual: Role) {
        Assertions.assertEquals(expected.id, actual.id)
        Assertions.assertEquals(expected.permissions, actual.permissions)
        Assertions.assertEquals(expected.updatedBy, actual.updatedBy)
        Assertions.assertEquals(expected.createdBy, actual.createdBy)
    }
}


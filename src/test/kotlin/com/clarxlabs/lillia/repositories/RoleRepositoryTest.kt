package com.clarxlabs.lillia.repositories

import com.clarxlabs.lillia.entities.Role
import com.clarxlabs.lillia.support.insertRole
import com.clarxlabs.lillia.support.paginationAssertion
import com.clarxlabs.lillia.support.roleAssertions
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

@MicronautTest
class RoleRepositoryTest {

    @Inject
    lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun cleanup() {
        roleRepository.deleteAll().block()
    }

    @Test
    fun `should find all roles with pagination`() {
        val pageable = Pageable.from(0, 2, Sort.of(Sort.Order.asc("createdAt")))
        val roles = (0 until 2).map { insertRole(roleRepository) }.sortedBy { it.createdAt }

        StepVerifier.create(roleRepository.findAll(pageable))
            .assertNext { paginationAssertion(pageable, it, roles, ::roleAssertions) }
            .verifyComplete()
    }

    @Test
    fun `should save the role when it is valid`() {
        val role = Role("witcher", setOf("GET:roles/", "POST:roles"), "wizard", "wizard")

        StepVerifier.create(roleRepository.save(role))
            .assertNext { roleAssertions(role, it) }
            .verifyComplete()
    }

    @Test
    fun `should not save the role when it is invalid`() {
        val role = Role("", setOf("INVALID-R0le!"))

//        reactive repositories with validation are not returning Mono.error() yet
//
//        StepVerifier.create(roleRepository.save(role))
//            .expectError(ConstraintViolationException::class.java)
//            .verify()

        Assertions.assertThrows(ConstraintViolationException::class.java) { roleRepository.save(role) }
    }

    @Test
    fun `should find the role when the id exists`() {
        val role = insertRole(roleRepository)

        StepVerifier.create(roleRepository.findById(role.id))
            .assertNext { roleAssertions(role, it) }
            .verifyComplete()
    }

    @Test
    fun `should not find the role when the id does not exist`() {
        StepVerifier.create(roleRepository.findById("not_found_id"))
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun `should delete the role when the id exists`() {
        val role = insertRole(roleRepository)

        StepVerifier.create(roleRepository.deleteById(role.id))
            .assertNext { Assertions.assertEquals(1, it) }
            .verifyComplete()

        StepVerifier.create(roleRepository.findById(role.id))
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun `should not delete any role when the id does not exist`() {
        StepVerifier.create(roleRepository.deleteById("not_found_id"))
            .assertNext { Assertions.assertEquals(0, it) }
            .verifyComplete()
    }
}

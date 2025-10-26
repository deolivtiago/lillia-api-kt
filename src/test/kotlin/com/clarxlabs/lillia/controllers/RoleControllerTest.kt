package com.clarxlabs.lillia.controllers

import com.clarxlabs.lillia.controllers.dtos.PagedList
import com.clarxlabs.lillia.entities.Role
import com.clarxlabs.lillia.repositories.RoleRepository
import com.clarxlabs.lillia.support.insertRole
import com.clarxlabs.lillia.support.roleAssertions
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import kotlin.random.Random

@MicronautTest(transactional = false)
class RoleControllerTest {
    @Inject
    @field:Client("/roles")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun cleanup() {
        roleRepository.deleteAll().block()
    }

    @Test
    fun `should list all roles with pagination`() {
        val roles = listOf(insertRole(roleRepository))
        val pagination = PagedList.Pagination(totalPages = roles.size, totalItems = roles.size.toLong())

        val request = HttpRequest.GET<Any>(
            UriBuilder.of("/")
                .queryParam("size", pagination.size).queryParam("page", pagination.page)
                .build()
        )

        StepVerifier
            .create(httpClient.retrieve(request, Argument.of(PagedList::class.java, Role::class.java)))
            .assertNext { Assertions.assertEquals(PagedList(roles, pagination), it) }
            .verifyComplete()
    }

    @Test
    fun `should get the role by its id`() {
        val role = insertRole(roleRepository)

        val request = HttpRequest.GET<Any>(
            UriBuilder.of("/")
                .path(role.id)
                .build()
        )

        StepVerifier
            .create(httpClient.retrieve(request, Role::class.java))
            .assertNext { roleAssertions(role, it) }
            .verifyComplete()
    }

    @Test
    fun `should create a new role with permissions`() {
        val input = Role("fairy_lvl" + Random.nextInt(1, 999), setOf("GET:roles/", "POST:roles"))

        val request = HttpRequest.POST<Any>(
            UriBuilder.of("/").build(),
            input
        )

        StepVerifier
            .create(httpClient.retrieve(request, Role::class.java))
            .assertNext { roleAssertions(input, it) }
            .verifyComplete()
    }

    @Test
    fun `should create a new role with no permissions`() {
        val input = Role("fairy_lvl" + Random.nextInt(1, 999))

        val request = HttpRequest.POST<Any>(
            UriBuilder.of("/").build(),
            input
        )

        StepVerifier
            .create(httpClient.retrieve(request, Role::class.java))
            .assertNext { roleAssertions(input, it) }
            .verifyComplete()
    }
}

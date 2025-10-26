package com.clarxlabs.lillia.support

import com.clarxlabs.lillia.entities.Role
import com.clarxlabs.lillia.repositories.RoleRepository
import java.time.ZonedDateTime
import kotlin.random.Random

fun insertRole(
    repository: RoleRepository,
    id: String = "fairy_lvl" + Random.nextInt(1, 999),
    permissions: Set<String> = emptySet(),
    createdBy: String = "wizard",
    updatedBy: String = "wizard",
    createdAt: ZonedDateTime? = null,
    updatedAt: ZonedDateTime? = null,
): Role = repository.save(Role(id, permissions, createdBy, updatedBy, createdAt, updatedAt)).block()!!


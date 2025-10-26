package com.clarxlabs.lillia.support

import com.clarxlabs.lillia.entities.Role
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.junit.jupiter.api.Assertions

fun roleAssertions(expected: Role, actual: Role) {
    Assertions.assertEquals(expected.id, actual.id)
    Assertions.assertEquals(expected.permissions, actual.permissions)
    Assertions.assertEquals(expected.updatedBy, actual.updatedBy)
    Assertions.assertEquals(expected.createdBy, actual.createdBy)
}

fun <T> paginationAssertion(
    pageable: Pageable,
    page: Page<T>,
    items: Iterable<T>,
    itemAssertion: (T, T) -> Unit
) {
    Assertions.assertEquals(pageable.number, page.pageNumber)
    Assertions.assertEquals(pageable.size, page.size)
    Assertions.assertEquals(pageable.sort.orderBy, page.sort.orderBy)
    page.content.forEachIndexed { idx, it -> itemAssertion(items.elementAt(idx), it) }
}

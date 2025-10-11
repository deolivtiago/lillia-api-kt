package com.clarxlabs.lillia.controllers.dtos

import io.micronaut.data.model.Page
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class PagedList<T>(val items: List<T>, val pagination: Pagination) {
    @Serdeable
    data class Pagination(
        val size: Int,
        val page: Int,
        val totalPages: Int,
        val totalItems: Long,
        val sort: List<Sorting> = listOf(),
    ) {
        @Serdeable
        data class Sorting(val orderBy: String = "createdAt", val isDesc: Boolean = true)

        companion object {
            fun <T> from(page: Page<T>): Pagination =
                Pagination(
                    size = page.size,
                    page = page.pageNumber,
                    totalPages = page.totalPages,
                    totalItems = page.totalSize,
                    sort = page.sort.orderBy.map { Sorting(it.property, !it.isAscending) }
                )
        }
    }

    companion object {
        fun <T> from(page: Page<T>): PagedList<T> = PagedList(items = page.content, pagination = Pagination.from(page))
    }
}

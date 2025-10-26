package com.clarxlabs.lillia.controllers.dtos

import io.micronaut.data.model.Page
import io.micronaut.data.model.Sort.Order
import io.micronaut.data.model.Sort.Order.Direction
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class PagedList<T>(val items: List<T>, val pagination: Pagination) {
    @Serdeable
    data class Pagination(
        val size: Int = 2,
        val page: Int = 0,
        val sort: List<Sorting> = emptyList(),
        val totalPages: Int = 0,
        val totalItems: Long = 0,
    ) {
        @Serdeable
        data class Sorting(val orderBy: String = "createdAt", val direction: Direction = Direction.DESC) {
            companion object {
                fun of(order: Order): Sorting = Sorting(order.property, order.direction)
            }
        }

        companion object {
            fun <T> of(page: Page<T>): Pagination =
                Pagination(
                    size = page.size,
                    page = page.pageNumber,
                    sort = page.sort.orderBy.map(Sorting::of),
                    totalPages = page.totalPages,
                    totalItems = page.totalSize,
                )
        }
    }

    companion object {
        fun <T> of(page: Page<T>): PagedList<T> = PagedList(items = page.content, pagination = Pagination.of(page))
    }
}

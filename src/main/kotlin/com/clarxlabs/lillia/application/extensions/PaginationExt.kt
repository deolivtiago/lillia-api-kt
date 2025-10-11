package com.clarxlabs.lillia.application.extensions

import com.clarxlabs.lillia.controllers.dtos.PagedList
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import reactor.core.publisher.Mono
import java.util.*
import kotlin.jvm.optionals.getOrNull

fun Pageable.copy(
    orderBy: List<Sort.Order> = this.sort.orderBy,
    number: Int = this.number,
    size: Int = this.size,
    mode: Pageable.Mode = this.mode,
    cursor: Optional<Pageable.Cursor> = this.cursor(),
    requestTotal: Boolean = this.requestTotal(),
): Pageable = Pageable.from(number, size, mode, cursor.getOrNull(), Sort.of(orderBy), requestTotal)

fun Iterable<Sort.Order>.filterProperties(predicate: (String) -> Boolean): List<Sort.Order> =
    filter { predicate(it.property) }

fun <T> Mono<Page<T>>.toPagedList(): Mono<PagedList<T>> =
    map { PagedList.from(it) }

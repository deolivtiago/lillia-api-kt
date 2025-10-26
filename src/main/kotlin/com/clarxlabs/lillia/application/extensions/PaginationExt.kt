package com.clarxlabs.lillia.application.extensions

import com.clarxlabs.lillia.controllers.dtos.PagedList
import io.micronaut.core.naming.NameUtils
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import reactor.core.publisher.Mono
import kotlin.jvm.optionals.getOrNull

fun <T> Mono<Page<T>>.toPagedList(): Mono<PagedList<T>> =
    map { PagedList.of(it) }

fun Pageable.filterSort(predicate: (String) -> Boolean): Pageable =
    Pageable.from(
        this.number,
        this.size,
        this.mode,
        this.cursor().getOrNull(),
        this.orderBy
            .map { Sort.Order(NameUtils.camelCase(it.property), it.direction, it.isIgnoreCase) }
            .filter { predicate(it.property) }
            .let(Sort::of),
        this.requestTotal()
    )

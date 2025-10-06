package com.clarxlabs.lillia.application.extensions

import jakarta.validation.ConstraintViolation

fun Iterable<ConstraintViolation<*>>.errors(): Map<String, List<String>> =
    map(ConstraintViolation<*>::toPair).groupBy(Pair<String, String>::first, Pair<String, String>::second)

fun <T> ConstraintViolation<T>.toPair(): Pair<String, String> =
    propertyPath.toString().replace(Regex("^.+[.]|\\W.+"), "") to message.replace(Regex("^.+:\\s"), "").lowercase()

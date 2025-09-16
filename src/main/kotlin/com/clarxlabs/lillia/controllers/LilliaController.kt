package com.clarxlabs.lillia.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/")
class LilliaController {
    @Get(uri = "/ping", produces = ["text/plain"])
    fun index(): String = "pong"
}

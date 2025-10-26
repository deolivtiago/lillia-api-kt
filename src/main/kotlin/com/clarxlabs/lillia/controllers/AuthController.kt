package com.clarxlabs.lillia.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/auth")
class AuthController {
}

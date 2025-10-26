package com.clarxlabs.lillia.application

import com.clarxlabs.lillia.entities.Token
import com.clarxlabs.lillia.entities.User
import com.clarxlabs.lillia.repositories.TokenRepository
import com.clarxlabs.lillia.repositories.UserRepository
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.micronaut.security.authentication.provider.ReactiveAuthenticationProvider
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import java.util.*

@Singleton
class AuthenticationProvider<B>(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : RefreshTokenPersistence, ReactiveAuthenticationProvider<HttpRequest<B>, String, String> {
    @SingleResult
    override fun authenticate(
        context: HttpRequest<B>, request: AuthenticationRequest<String, String>
    ): Publisher<AuthenticationResponse?>? {
        val username = request.getIdentity()
        val password = request.getSecret()

        return userRepository.findByUsername(username)
            .flatMap { if (it.password == password) userAuthenticated(it) else invalidCredentialError() }
            .switchIfEmpty(userNotFoundError())
    }

    override fun persistToken(it: RefreshTokenGeneratedEvent) {
        tokenRepository.save(Token.of(it)).subscribe()
    }

    override fun getAuthentication(id: String): Publisher<Authentication> =
        tokenRepository.findById(UUID.fromString(id))
            .flatMap { Mono.just(Authentication.build(it.username, it.roles)) }
            .switchIfEmpty(tokenNotFoundError())

    private fun tokenNotFoundError(): Mono<Authentication> = Mono.error(AuthenticationException())
    private fun userNotFoundError(): Mono<AuthenticationResponse> = Mono.just(AuthenticationFailed())
    private fun invalidCredentialError(): Mono<AuthenticationResponse> = Mono.just(AuthenticationFailed())
    private fun userAuthenticated(user: User): Mono<AuthenticationResponse> =
        Mono.just(AuthenticationResponse.success(user.username, setOf(user.roleId)))
}

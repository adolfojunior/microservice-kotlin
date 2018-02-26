package org.test.auth

import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthenticationToken(
		val token: String
) : AbstractAuthenticationToken(emptyList()) {

	override fun getPrincipal() = token

	override fun getCredentials() = token
}
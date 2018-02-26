package org.test.auth.user

import org.springframework.security.authentication.AbstractAuthenticationToken

class UserAuthenticationToken(
		val username: String? = null,
		val password: String? = null,
		val token: String? = null
) : AbstractAuthenticationToken(emptyList()) {

	override fun getPrincipal() = username

	override fun getCredentials() = password
}
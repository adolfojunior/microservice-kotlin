package org.test.auth.user

import org.springframework.security.authentication.AbstractAuthenticationToken
import java.util.Objects

class ApplicationUserDetails(
		var username: String,
		var tenant: String
)

class ApplicationUser(
		val username: String,
		val token: String,
		val details: ApplicationUserDetails
) : AbstractAuthenticationToken(emptyList()) {

	override fun getPrincipal() = username

	override fun getCredentials() = token

	override fun hashCode() = Objects.hash(username, token)
}

package org.test.auth.user

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.test.auth.JwtAuthenticationToken
import org.test.auth.user.ApplicationUser
import org.test.auth.user.UserService

class JwtAuthenticationProvider(val userService: UserService) : AuthenticationProvider {

	override fun supports(type: Class<*>?) = JwtAuthenticationToken::class.java.isAssignableFrom(type)

	override fun authenticate(auth: Authentication?): ApplicationUser {
		return (auth as JwtAuthenticationToken).let {
			userService.authenticateToken(auth.token)
		}.apply {
			isAuthenticated = true
		}
	}
}

package org.test.auth.user

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class UserAuthenticationProvider(val userService: UserService) : AuthenticationProvider {

	override fun supports(type: Class<*>?) = UserAuthenticationToken::class.java.isAssignableFrom(type)

	override fun authenticate(auth: Authentication?): Authentication? {
		return (auth as UserAuthenticationToken).let {
			userService.authenticate(auth.username!!, auth.password!!)
		}.apply {
			isAuthenticated = true
		}
	}
}

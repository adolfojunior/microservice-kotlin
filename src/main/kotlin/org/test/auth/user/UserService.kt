package org.test.auth.user

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.test.auth.JwtService
import org.test.auth.ObjectConverter

class UserService(val converter: ObjectConverter, val jwt: JwtService) {

	fun authenticate(username: String, password: String): ApplicationUser {
		// TODO go to database
		return findUser(username, password).let {
			ApplicationUser(
					username = username,
					details = it,
					token = createToken(it)
			)
		}

	}

	fun authenticateToken(token: String): ApplicationUser {
		return parseToken(token).let {
			ApplicationUser(
					username = it.username,
					token = token,
					details = it
			)
		}
	}

	fun findUser(username: String, password: String): ApplicationUserDetails {
		return if (username.equals(password)) {
			ApplicationUserDetails(username, "TENANT")
		} else throw BadCredentialsException("invalid credentials")
	}

	fun currentUser(): ApplicationUser? = SecurityContextHolder.getContext().authentication as ApplicationUser

	protected fun createToken(details: ApplicationUserDetails): String = converter.toValues(details).let(jwt::token)

	protected fun parseToken(token: String): ApplicationUserDetails = jwt.parse(token).let { converter.toObject(it, ApplicationUserDetails::class) }
}
package org.test.auth.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UsernameAndPassword(var username: String?, var password: String?) {

	fun isFilled() = !username.isNullOrBlank() || !password.isNullOrBlank()
}

class UserAuthenticationFilter(var mapper: ObjectMapper) : UsernamePasswordAuthenticationFilter() {

	private val log = LoggerFactory.getLogger(UserAuthenticationFilter::class.java)

	init {
		setAuthenticationSuccessHandler(AuthenticationSuccessHandler { _, _, auth ->
			val user = (auth as ApplicationUser)
			log.info("User authenticated: {} -> {}", user.username, user.token)
		})
	}

	@Throws(IOException::class, AuthenticationException::class)
	override fun attemptAuthentication(request: HttpServletRequest,
									   response: HttpServletResponse): Authentication {

		if (!HttpMethod.POST.name.equals(request.getMethod(), ignoreCase = true)) {
			throw AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		return readUser(request).let {
			UserAuthenticationToken(username = it.username, password = it.password)
		}.let {
			authenticationManager.authenticate(it)
		}
	}

	@Throws(IOException::class, AuthenticationException::class)
	fun readUser(request: HttpServletRequest) = try {
		mapper.readValue(request.inputStream, UsernameAndPassword::class.java)
	} catch (e: Exception) {
		throw BadCredentialsException("error trying to parse user", e)
	}.takeIf {
		it.isFilled()
	} ?: throw BadCredentialsException("invalid user")

	@Throws(IOException::class, AuthenticationException::class)
	override fun successfulAuthentication(request: HttpServletRequest,
										  response: HttpServletResponse,
										  chain: FilterChain, authResult: Authentication) {
		if (authResult.isAuthenticated) {
			// propatgate the authentication to SecurityContext
			super.successfulAuthentication(request, response, chain, authResult)
			// after success we need to continue the request normally
			chain.doFilter(request, response)
		}
	}
}
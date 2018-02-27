package org.test.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.util.matcher.RequestMatcher
import org.test.auth.user.ApplicationUser
import org.test.auth.user.UserAuthenticationToken
import java.io.IOException
import javax.naming.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.test.auth.ObjectConverter
import org.test.auth.JwtAuthenticationToken

object AuthorizationBearer : RequestMatcher {

	val name = "Authorization"
	val pattern = Regex("Bearer\\s+(.+)", option = RegexOption.IGNORE_CASE)

	fun get(r: HttpServletRequest): String? = r.getHeader(name)

	fun token(r: HttpServletRequest): String? {
		return get(r).takeUnless {
			it.isNullOrBlank()
		}?.let {
			pattern.find(it)?.groupValues?.get(1)
		}
	}

	override fun matches(request: HttpServletRequest): Boolean {
		return !token(request).isNullOrBlank()
	}
}

class JwtAuthenticationFilter : AbstractAuthenticationProcessingFilter(AuthorizationBearer) {

	private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

	init {
		setAuthenticationSuccessHandler(AuthenticationSuccessHandler { _, _, auth ->
			val user = (auth as ApplicationUser)
			log.info("JWT authenticated: {} -> {}", user.username, user.token)
		})
	}

	@Throws(IOException::class, AuthenticationException::class)
	override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
		return AuthorizationBearer.token(request).takeUnless {
			it.isNullOrBlank()
		}.let {
			JwtAuthenticationToken(it!!)
		}.let {
			authenticationManager.authenticate(it)
		}
	}

	@Throws(IOException::class, AuthenticationException::class)
	override fun successfulAuthentication(request: HttpServletRequest,
										  response: HttpServletResponse,
										  chain: FilterChain, auth: Authentication) {
		if (auth.isAuthenticated) {
			// propatgate the authentication to SecurityContext
			super.successfulAuthentication(request, response, chain, auth)
			// after success we need to continue the request normally
			chain.doFilter(request, response)
		}
	}
}
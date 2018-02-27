package org.test.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.test.auth.jwt.JwtAuthenticationFilter
import org.test.auth.user.UserAuthenticationFilter
import org.test.auth.user.JwtAuthenticationProvider
import org.test.auth.user.UserAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder

class WebSecurityConfigurer(
		val converter: ObjectConverter,
		val jwtProvider: JwtAuthenticationProvider,
		val userProvider: UserAuthenticationProvider
) : WebSecurityConfigurerAdapter(true) {

	fun jwtFilter() = JwtAuthenticationFilter().apply {
		setAuthenticationManager(authenticationManager())
	}

	fun userFilter() = UserAuthenticationFilter(converter).apply {
	  setAuthenticationManager(authenticationManager())
	}
	
	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.authenticationProvider(jwtProvider).authenticationProvider(userProvider)
	}

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		// dsable Cross-site request forgery
		http.csrf().disable()
		// enable Cross-Origin Resource Sharing
		http.cors()
		// permit /actuator endpoints
		http.authorizeRequests().antMatchers("/actuator/**").permitAll()
		// all other requests must be authenticated
		http.authorizeRequests().anyRequest().authenticated()
		// register custom authentication filter
		http.addFilterBefore(userFilter(), UsernamePasswordAuthenticationFilter::class.java)
		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
		// make session statless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	}
}
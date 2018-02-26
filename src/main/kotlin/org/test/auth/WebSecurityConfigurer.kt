package org.test.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.test.auth.jwt.JwtAuthenticationFilter
import org.test.auth.user.UserAuthenticationFilter

class WebSecurityConfigurer(val mapper: ObjectMapper) : WebSecurityConfigurerAdapter(false) {

	fun userFilter() = UserAuthenticationFilter(mapper).apply {
		setAuthenticationManager(authenticationManager())
	}
	
	fun jwtFilter() = JwtAuthenticationFilter(mapper).apply {
	  setAuthenticationManager(authenticationManager())
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
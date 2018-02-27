package org.test.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.test.auth.jwt.JwtProperties
import org.test.auth.user.JwtAuthenticationProvider
import org.test.auth.user.UserAuthenticationProvider
import org.test.auth.user.UserService

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
open class SecurityConfiguration {

	@Bean
	open fun converter(mapper: ObjectMapper) = ObjectConverter(mapper)

	@Bean
	open fun jwtService(jwtProperties: JwtProperties) = JwtService(jwtProperties)

	@Bean
	open fun userService(converter: ObjectConverter, jwtService: JwtService) = UserService(converter, jwtService)

	@Bean
	open fun jwtProvider(userService: UserService) = JwtAuthenticationProvider(userService)

	@Bean
	open fun userProvider(userService: UserService) = UserAuthenticationProvider(userService)

	@Bean
	open fun webSecurityConfigurer(converter: ObjectConverter, jwtProvider: JwtAuthenticationProvider, userProvider: UserAuthenticationProvider): WebSecurityConfigurerAdapter = WebSecurityConfigurer(converter, jwtProvider, userProvider)

}
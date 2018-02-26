package org.test.auth

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.stereotype.Component
import org.test.auth.user.ApplicationUser

@Component
class UserAuthenticationListener {

	private val log = LoggerFactory.getLogger(UserAuthenticationListener::class.java)

	@EventListener
	fun login(event: InteractiveAuthenticationSuccessEvent) {
		val auth = event.authentication
		log.info("User authenticated: {}", event)
		if (auth is ApplicationUser) {
		  // 	SHARE TENANT INFO
			log.info("Will apply tenatn: {}", auth.details.tenant)
		}
	}
}
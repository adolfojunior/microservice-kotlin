package org.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.test.auth.user.UserService

@RestController
@RequestMapping("test")
class TestController(val userService: UserService) {

	@GetMapping
	fun get() = userService.currentUser()?.toString()
}

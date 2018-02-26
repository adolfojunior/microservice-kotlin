package org.test.auth.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

class UserToken(val token: String?)

@RestController
@RequestMapping("login")
class UserLoginController(val userService: UserService) {

	@PostMapping
	fun login() = UserToken(userService.currentUser()?.token)
}
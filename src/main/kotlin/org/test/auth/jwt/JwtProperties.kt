package org.test.auth.jwt

import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("dart.auth.jwt")
class JwtProperties(
		var secret: String?,
		var algorithm: SignatureAlgorithm?
)
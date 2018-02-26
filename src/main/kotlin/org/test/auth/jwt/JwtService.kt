package org.test.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.charset.StandardCharsets
import io.jsonwebtoken.JwtBuilder
import java.time.Instant
import org.test.auth.jwt.JwtProperties

class JwtService(val jwtProperties: JwtProperties) {

	fun token(claims: Map<String, *>): String = builder().setClaims(claims).compact()

	fun parse(token: String): Map<String, *> = parser().parseClaimsJws(token).getBody()

	protected fun builder() : JwtBuilder = Jwts.builder().signWith(algorithm(), secret())

	protected fun parser() = Jwts.parser().setSigningKey(secret())

	protected fun algorithm() = jwtProperties.algorithm ?: SignatureAlgorithm.HS256

	protected fun secret(): ByteArray = jwtProperties.secret?.toByteArray(StandardCharsets.UTF_8) ?: throw IllegalStateException("unknow secret")
}
package org.test.auth

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KClass

class MapType : TypeReference<Map<String, *>>()

class ObjectConverter(val mapper: ObjectMapper) {

	@Throws(IOException::class)
	fun jsonString(value: Any) = mapper.writeValueAsString(value)

	@Throws(IOException::class)
	fun writeJson(output: OutputStream, value: Any) = mapper.writeValue(output, value)

	@Throws(IOException::class)
	fun <T : Any> readJson(input: InputStream, type: KClass<T>): T = mapper.readValue(input, type.java)

	fun <T : Any> toObject(values: Map<String, *>, type: KClass<T>): T = mapper.convertValue(values, type.java)

	fun toValues(values: Any): Map<String, *> = mapper.convertValue(values, MapType())
}
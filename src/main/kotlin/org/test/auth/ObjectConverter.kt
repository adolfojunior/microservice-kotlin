package org.test.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference
import kotlin.reflect.KClass

class MapType : TypeReference<Map<String, *>>()

class ObjectConverter(val mapper: ObjectMapper) {

	fun <T:Any> toObject(values: Map<String, *>, type: KClass<T>): T = mapper.convertValue(values, type.java)
	
	fun toValues(values: Any): Map<String, *> = mapper.convertValue(values, MapType())
}
package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONParser {

	fun parseList(source: JSONReader, closeSource: Boolean = true) =
		parseValueAsType<List<*>>(source, closeSource = closeSource)


	fun parseMap(source: JSONReader, closeSource: Boolean = true) =
		parseValueAsType<Map<String, *>>(source, closeSource = closeSource)


	private inline fun <reified ReturnValue> parseValueAsType(source: JSONReader, closeSource: Boolean): ReturnValue {
		val value = parseValueOrNull(source, closeSource = closeSource)
		return value as? ReturnValue
			?: throw JSONException("cannot parse ${ReturnValue::class}, got " + value?.let { "${value::class}: $value" })
	}


	fun parseValueOrNull(source: JSONReader, closeSource: Boolean = true): Any?


	companion object {

		val default: JSONParser get() = StandardParser
	}
}


fun JSONParser.parseList(source: Reader, closeSource: Boolean = true) =
	parseList(JSONReader.build(source), closeSource = closeSource)


fun JSONParser.parseList(source: String) =
	parseList(JSONReader.build(source))


fun JSONParser.parseMap(source: Reader, closeSource: Boolean = true) =
	parseMap(JSONReader.build(source), closeSource = closeSource)


fun JSONParser.parseMap(source: String) =
	parseMap(JSONReader.build(source))


fun JSONParser.parseValueOrNull(source: Reader, closeSource: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), closeSource = closeSource)


fun JSONParser.parseValueOrNull(source: String) =
	parseValueOrNull(JSONReader.build(source))

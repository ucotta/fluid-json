package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONCodingParser : JSONParser {

	override fun parseList(source: JSONReader, closeSource: Boolean) =
		parseValueOfType<List<*>>(source, closeSource = closeSource)


	override fun parseMap(source: JSONReader, closeSource: Boolean) =
		parseValueOfType<Map<String, *>>(source, closeSource = closeSource)


	fun <Value : Any> parseValueOfTypeOrNull(source: JSONReader, valueType: JSONCodableType<Value>, closeSource: Boolean = true): Value?


	override fun parseValueOrNull(source: JSONReader, closeSource: Boolean) =
		parseValueOfTypeOrNull<Any>(source, closeSource = closeSource)


	companion object {

		fun builder(): BuilderForDecoding<JSONCodingContext> =
			BuilderForDecodingImpl(context = JSONCodingContext.empty)


		fun <Context : JSONCodingContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		val default = builder()
			.decodingWith()
			.build()


		val nonRecursive = builder()
			.decodingWith(DefaultJSONCodecs.nonRecursive)
			.build()


		interface BuilderForDecoding<Context : JSONCodingContext> {

			fun decodingWith(factory: (source: JSONReader, context: Context) -> JSONDecoder<Context>): Builder


			fun decodingWith(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				decodingWith(providers = providers.toList(), base = base)


			fun decodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				decodingWith { source, context ->
					JSONDecoder.builder(context)
						.codecs(JSONCodecProvider.of(providers = providers, base = base))
						.source(source)
						.build()
				}
		}


		private class BuilderForDecodingImpl<Context : JSONCodingContext>(
			private val context: Context
		) : BuilderForDecoding<Context> {

			override fun decodingWith(factory: (source: JSONReader, context: Context) -> JSONDecoder<Context>) =
				BuilderImpl(
					context = context,
					decoderFactory = factory
				)
		}


		interface Builder {

			fun build(): JSONCodingParser
		}


		private class BuilderImpl<out Context : JSONCodingContext>(
			private val context: Context,
			private val decoderFactory: (source: JSONReader, context: Context) -> JSONDecoder<Context>
		) : Builder {

			override fun build() =
				StandardCodingParser(
					context = context,
					decoderFactory = decoderFactory
				)
		}
	}
}


fun JSONCodingParser.parseValue(source: JSONReader, closeSource: Boolean = true) =
	parseValueOrNull(source, closeSource = closeSource) ?: throw JSONException("unexpected null value at top-level")


fun JSONCodingParser.parseValue(source: Reader, closeSource: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), closeSource = closeSource)


fun JSONCodingParser.parseValue(source: String) =
	parseValue(JSONReader.build(source))


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: JSONReader, closeSource: Boolean = true): Value =
	parseValueOfType(source, valueType = jsonCodableType(), closeSource = closeSource)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: Reader, closeSource: Boolean = true): Value =
	parseValueOfType(JSONReader.build(source), closeSource = closeSource)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: String): Value =
	parseValueOfType(JSONReader.build(source))


fun <Value : Any> JSONCodingParser.parseValueOfType(source: JSONReader, valueType: JSONCodableType<Value>, closeSource: Boolean = true) =
	parseValueOfTypeOrNull(source, valueType = valueType, closeSource = closeSource) ?: throw JSONException("unexpected null value at top-level")


fun <Value : Any> JSONCodingParser.parseValueOfType(source: Reader, valueType: JSONCodableType<Value>, closeSource: Boolean = true) =
	parseValueOfType(JSONReader.build(source), valueType = valueType, closeSource = closeSource)


fun <Value : Any> JSONCodingParser.parseValueOfType(source: String, valueType: JSONCodableType<Value>): Value =
	parseValueOfType(JSONReader.build(source), valueType = valueType)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: JSONReader, closeSource: Boolean = true): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodableType(), closeSource = closeSource)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: Reader, closeSource: Boolean = true): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), closeSource = closeSource)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source))


fun <Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: Reader, valueType: JSONCodableType<Value>, closeSource: Boolean = true): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), valueType = valueType, closeSource = closeSource)


fun <Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: String, valueType: JSONCodableType<Value>): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), valueType = valueType)


fun JSONCodingParser.parseValueOrNull(source: Reader, closeSource: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), closeSource = closeSource)


fun JSONCodingParser.parseValueOrNull(source: String) =
	parseValueOrNull(JSONReader.build(source))

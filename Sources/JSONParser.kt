package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


interface JSONParser {

	fun <Value : Any> parseValueOfTypeOrNull(source: Reader, valueType: JSONCodableType<Value>): Value?


	companion object {

		fun builder(): BuilderForDecoding<JSONCoderContext> =
			BuilderForDecodingImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		val default = builder()
			.decodingWith()
			.build()


		val nonRecursive = builder()
			.decodingWith(DefaultJSONCodecs.nonRecursive)
			.build()


		interface BuilderForDecoding<Context : JSONCoderContext> {

			fun decodingWith(factory: (source: Reader, context: Context) -> JSONDecoder<Context>): Builder


			fun decodingWith(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCoderContext>? = JSONCodecProvider.extended
			) =
				decodingWith(providers = providers.toList(), base = base)


			fun decodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCoderContext>? = JSONCodecProvider.extended
			) =
				decodingWith { source, context ->
					JSONDecoder.builder(context)
						.codecs(JSONCodecProvider.of(providers = providers, base = base))
						.source(source)
						.build()
				}
		}


		private class BuilderForDecodingImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForDecoding<Context> {

			override fun decodingWith(factory: (source: Reader, context: Context) -> JSONDecoder<Context>) =
				BuilderImpl(
					context = context,
					decoderFactory = factory
				)
		}


		interface Builder {

			fun build(): JSONParser
		}


		private class BuilderImpl<out Context : JSONCoderContext>(
			private val context: Context,
			private val decoderFactory: (source: Reader, context: Context) -> JSONDecoder<Context>
		) : Builder {

			override fun build() =
				StandardParser(
					context = context,
					decoderFactory = decoderFactory
				)
		}
	}
}


fun JSONParser.parseValue(source: Reader) =
	parseValueOrNull(source) ?: throw JSONException("Unexpected null value at top-level")


fun JSONParser.parseValue(source: String) =
	parseValue(StringReader(source))


inline fun <reified Value : Any> JSONParser.parseValueOfType(source: Reader): Value =
	parseValueOfType(source, valueType = jsonCodableType())


inline fun <reified Value : Any> JSONParser.parseValueOfType(source: String): Value =
	parseValueOfType(StringReader(source))


fun <Value : Any> JSONParser.parseValueOfType(source: Reader, valueType: JSONCodableType<Value>) =
	parseValueOfTypeOrNull(source, valueType = valueType) ?: throw JSONException("Unexpected null value at top-level")


fun <Value : Any> JSONParser.parseValueOfType(source: String, valueType: JSONCodableType<Value>): Value =
	parseValueOfType(StringReader(source), valueType = valueType)


inline fun <reified Value : Any> JSONParser.parseValueOfTypeOrNull(source: Reader): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodableType())


inline fun <reified Value : Any> JSONParser.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(StringReader(source))


fun <Value : Any> JSONParser.parseValueOfTypeOrNull(source: String, valueType: JSONCodableType<Value>): Value? =
	parseValueOfTypeOrNull(StringReader(source), valueType = valueType)


fun JSONParser.parseValueOrNull(source: Reader) =
	parseValueOfTypeOrNull<Any>(source)


fun JSONParser.parseValueOrNull(source: String) =
	parseValueOrNull(StringReader(source))

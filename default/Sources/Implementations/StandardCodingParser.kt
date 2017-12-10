package com.github.fluidsonic.fluid.json


internal class StandardCodingParser<out Context : JSONCodingContext>(
	private val context: Context,
	private val decoderFactory: (source: JSONReader, context: Context) -> JSONDecoder<Context>
) : JSONCodingParser {

	override fun <Value : Any> parseValueOfTypeOrNull(source: JSONReader, valueType: JSONCodableType<Value>, closeSource: Boolean) =
		decoderFactory(source, context).use { decoder ->
			// FIXME closeSource!
			decoder.readValueOfTypeOrNull(valueType).also { decoder.readEndOfInput() }
		}
}

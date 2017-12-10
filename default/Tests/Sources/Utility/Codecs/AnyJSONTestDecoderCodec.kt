package tests

import com.github.fluidsonic.fluid.json.*


internal object AnyJSONTestDecoderCodec : AbstractJSONDecoderCodec<Any, JSONCodingContext>(
	additionalProviders = listOf(BooleanJSONCodec, ListJSONDecoderCodec, MapJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodableType<in Any>, decoder: JSONDecoder<JSONCodingContext>) =
		AnyJSONDecoderCodec.decode(valueType, decoder)
}

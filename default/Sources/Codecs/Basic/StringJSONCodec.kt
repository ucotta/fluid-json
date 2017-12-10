package com.github.fluidsonic.fluid.json


object StringJSONCodec : AbstractJSONCodec<String, JSONCodingContext>() {

	override fun decode(valueType: JSONCodableType<in String>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString()


	override fun encode(value: String, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value)
}

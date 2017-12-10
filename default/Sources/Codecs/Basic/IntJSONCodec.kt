package com.github.fluidsonic.fluid.json


object IntJSONCodec : AbstractJSONCodec<Int, JSONCodingContext>() {

	override fun decode(valueType: JSONCodableType<in Int>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readInt()


	override fun encode(value: Int, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeInt(value)
}

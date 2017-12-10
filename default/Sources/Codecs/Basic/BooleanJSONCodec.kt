package com.github.fluidsonic.fluid.json


object BooleanJSONCodec : AbstractJSONCodec<Boolean, JSONCodingContext>() {

	override fun decode(valueType: JSONCodableType<in Boolean>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readBoolean()


	override fun encode(value: Boolean, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeBoolean(value)
}

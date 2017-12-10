package com.github.fluidsonic.fluid.json


object ListJSONDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>() {

	override fun decode(valueType: JSONCodableType<in List<*>>, decoder: JSONDecoder<JSONCodingContext>): List<*> {
		val elementType = valueType.arguments.single()

		return decoder.readListByElement {
			readValueOfTypeOrNull(elementType)
		}
	}


	val nonRecursive = NonRecursiveJSONDecoderCodec.create<List<*>>()
}

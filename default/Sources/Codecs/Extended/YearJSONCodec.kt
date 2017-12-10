package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Year


object YearJSONCodec : AbstractJSONCodec<Year, JSONCodingContext>() {

	override fun decode(valueType: JSONCodableType<in Year>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readInt().let { raw ->
			try {
				Year.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Invalid Year value: $raw")
			}
		}


	override fun encode(value: Year, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeInt(value.value)
}

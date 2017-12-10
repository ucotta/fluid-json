package tests

import com.github.fluidsonic.fluid.json.*
import org.jetbrains.spek.api.Spek


internal object CompanionLoaderSpec : Spek({

	// just make sure all companions initialize properly and we get more code coverage :)
	listOf(
		JSONCodableType.Companion,
		JSONCodec.Companion,
		JSONCodecProvider.Companion,
		JSONCodingContext.Companion,
		JSONCodingParser.Companion,
		JSONCodingSerializer.Companion,
		JSONDecoder.Companion,
		JSONDecoderCodec.Companion,
		JSONEncoder.Companion,
		JSONEncoderCodec.Companion
	)
})

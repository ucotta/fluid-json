package com.github.fluidsonic.fluid.json


sealed class JSONNullability {

	object None : JSONNullability()
	object Key : JSONNullability()
	object KeyAndValue : JSONNullability()
	object Value : JSONNullability()


	companion object
}
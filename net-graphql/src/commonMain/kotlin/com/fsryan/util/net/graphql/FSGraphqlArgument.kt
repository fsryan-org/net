package com.fsryan.util.net.graphql

/**
 * [A GraphqlArgument](https://spec.graphql.org/draft/#sec-Language.Arguments).
 *
 * The factory functions in the [FSGraphqlArgument.Companion] object allow for
 * both literal and ref instances to be created.
 */
sealed interface FSGraphqlArgument: FSGraphqlItem {
    // TODO: make sure you understand how floating point values are serialized.
    //  The below assumes that NaN is not input . . . which is bad.
    //  Additionally we risk loss of precision.
    companion object {
        fun literal(name: String, booleanValue: Boolean?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = booleanValue)
        }
        fun listLiteral(name: String, vararg booleanValues: Boolean?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = booleanValues.toList())
        }
        fun literal(name: String, doubleValue: Double?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = doubleValue)
        }
        fun listLiteral(name: String, vararg doubleValues: Double?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = doubleValues.toList())
        }
        fun literal(name: String, floatValue: Float?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = floatValue)
        }
        fun listLiteral(name: String, vararg floatValues: Float?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = floatValues.toList())
        }
        fun literal(name: String, intValue: Int?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = intValue)
        }
        fun listLiteral(name: String, vararg intValues: Int?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = intValues.toList())
        }
        fun literal(name: String, longValue: Long?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = longValue)
        }
        fun listLiteral(name: String, vararg longValues: Long?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = longValues.toList())
        }
        fun literal(name: String, value: String?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = value, formatter = ::stringValueFormatter)
        }
        fun listLiteral(name: String, vararg values: String?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = values.toList(), formatter = ::stringValueFormatter)
        }
        fun enumLiteral(name: String, value: String?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlArgumentImpl(name, value = value)
        }
        fun enumListLiteral(name: String, vararg values: String?): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlListArgument(name, literal = values.toList())
        }
        fun custom(name: String, customSerialization: () -> String): FSGraphqlArgument {
            return FSGraphqlCustomArgument(name, customSerialization = customSerialization)
        }
        fun variableRef(name: String, variableName: String): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            FSGraphqlNamed.checkName(variableName)
            return FSGraphqlVariableArgument(name, variableName = variableName)
        }
        fun variableRefList(name: String, vararg variableNames: String): FSGraphqlArgument {
            FSGraphqlNamed.checkName(name)
            variableNames.forEach(FSGraphqlNamed::checkName)
            return FSGraphqlListArgument(name, literal = variableNames.toList()) { "\$$it" }
        }
        private fun stringValueFormatter(str: String?): String {
            return str?.let { "\"$str\"" } ?: "null"
        }
    }
}

internal data class FSGraphqlCustomArgument(
    override val name: String,
    private val customSerialization: () -> String
): FSGraphqlArgument {
    override fun serialize(): String {
        return customSerialization()
    }
}

internal data class FSGraphqlArgumentImpl<T:Any?>(
    override val name: String,
    private val value: T?,
    private val formatter: (T?) -> String = { it?.toString() ?: "null" }
): FSGraphqlArgument {
    override fun serialize(): String = "$name: ${formatter(value)}"
}

internal data class FSGraphqlListArgument<T:Any?>(
    override val name: String,
    private val literal: List<T?>,
    private val formatter: (T?) -> String = { value -> value?.toString() ?: "null" }
): FSGraphqlArgument {
    override fun serialize(): String {
        val values = literal.joinToString(separator = ",", transform = formatter)
        return "$name: [$values]"
    }
}

internal data class FSGraphqlVariableArgument(
    override val name: String,
    private val variableName: String
): FSGraphqlArgument {
    override fun serialize(): String = "$name: \$$variableName"
}
package io.pelle.kitchensink.model

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

public class ModelTestBedExtension : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        parameterContext.parameter.type.isAssignableFrom(ModelTestBed::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        ModelTestBed()
}
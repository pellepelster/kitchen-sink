package io.pelle.todo.user.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.pelle.todo.util.MetaResponse

data class UserRegistrationResponse(@JsonProperty("_meta") val meta: MetaResponse?)

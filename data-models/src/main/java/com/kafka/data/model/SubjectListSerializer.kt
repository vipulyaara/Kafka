package com.kafka.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SubjectListSerializer : KSerializer<List<String>> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun deserialize(decoder: Decoder): List<String> {
        val surrogate = try {
            decoder.decodeSerializableValue(ListSerializer(String.serializer()))
                .joinToString(";")
        } catch (ex: Exception) {
            decoder.decodeSerializableValue(String.serializer())
        }
        return surrogate.split(";")
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        val surrogate = value.joinToString(";")
        encoder.encodeSerializableValue(String.serializer(), surrogate)
    }
}

package com.wisermit.hdrswitcher.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.serializersModuleOf
import java.io.File
import java.io.InputStream
import java.io.OutputStream

abstract class VersionedJsonSerializer<T> : Serializer<T> {

    protected val json: Json = Json {
        serializersModule = Serializers.module
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    abstract val version: Int
    abstract val serializer: KSerializer<T>

    open fun onVersionChanged(data: JsonElement, dataVersion: Int): T = defaultValue

    override suspend fun readFrom(input: InputStream): T {
        val inputString = input.bufferedReader().use { it.readText() }

        if (inputString.isBlank()) {
            return defaultValue
        }

        return try {
            val jsonData: VersionedData = json.decodeFromString(inputString)
            if (jsonData.version == version) {
                json.decodeFromJsonElement(serializer, jsonData.data)
            } else {
                onVersionChanged(jsonData.data, jsonData.version)
            }
        } catch (e: SerializationException) {
            throw CorruptionException(e.message.orEmpty(), e)
        }
    }

    final override suspend fun writeTo(t: T, output: OutputStream) {
        val versionedData = VersionedData(
            version = version,
            data = json.encodeToJsonElement(serializer, t),
        )
        val stringJson = json.encodeToString(versionedData)
        output.bufferedWriter().use { it.write(stringJson) }
    }
}

@Serializable
private data class VersionedData(
    @SerialName("version")
    val version: Int,

    @SerialName("data")
    val data: JsonElement,
)

private object Serializers {
    val module = serializersModuleOf(
        object : KSerializer<File> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("File", PrimitiveKind.STRING)

            override fun serialize(encoder: Encoder, value: File) = encoder.encodeString(value.path)
            override fun deserialize(decoder: Decoder) = File(decoder.decodeString())
        },
    )
}
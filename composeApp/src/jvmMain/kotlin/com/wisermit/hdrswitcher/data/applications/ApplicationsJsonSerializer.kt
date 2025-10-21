package com.wisermit.hdrswitcher.data.applications

import com.wisermit.hdrswitcher.data.VersionedJsonSerializer
import com.wisermit.hdrswitcher.model.Application
import kotlinx.serialization.serializer

object ApplicationsJsonSerializer : VersionedJsonSerializer<List<Application>>() {
    override val version = 1
    override val serializer = serializer<List<Application>>()
    override val defaultValue = emptyList<Application>()
}
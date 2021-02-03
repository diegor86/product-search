package com.diegor.productsearch.util

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceIdGenerator @Inject constructor() {

    fun generateDeviceId() = UUID.randomUUID().toString()

}
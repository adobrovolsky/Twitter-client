package com.twitty.store.entity

import java.io.Serializable
import java.util.*

data class Draft(var id: Int?, val text: String, val created: Date = Date()) : Serializable
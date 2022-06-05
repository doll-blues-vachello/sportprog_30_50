package ru.leadpogrommer.vk22.discord

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json





suspend fun main(){
    val client = CodeForcesClient()
    while (true){
        val line = readLine() ?: break
        val data = client.userInfoToString(line.trim())
        println(data)
    }
}
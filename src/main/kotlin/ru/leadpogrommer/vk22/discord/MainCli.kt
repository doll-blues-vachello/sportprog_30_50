package ru.leadpogrommer.vk22.discord


suspend fun mainCli(){
    val client = CodeForcesClient()
    while (true){
        print("Username>")
        val line = readLine() ?: break
        val data = client.userInfoToString(line.trim())
        println(data)
    }
}
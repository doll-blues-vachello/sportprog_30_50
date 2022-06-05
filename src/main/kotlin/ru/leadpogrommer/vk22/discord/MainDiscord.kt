package ru.leadpogrommer.vk22.discord

import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.reply
import dev.kord.core.cache.data.MessageData
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent


fun getSolvedTasks(submissions: List<CodeForcesSubmission>) = submissions.filter { it.verdict == "OK" }.map { it.problem }.toSet()


//class ProblemsRepo(val client: CodeForcesClient){
//    private var index = 0
//    private var nextContest = 0
//
//    val problems = mutableListOf<CodeForcesProblem>()
//
//    lateinit var contests: List<CodeForcesContest>
//
//    suspend fun init(){
//        println("Loading contests, this may take some time")
//        contests = client.loadListOfContests()
//        println("Loaded ${contests.size} contests")
//    }
//
//
//    fun reset(){
//        index = 0
//    }
//
//    suspend fun loadMore(){
//
//    }
//
//    suspend fun nextProblem(): CodeForcesProblem{
//        if(index >= problems.size){
//
//        }
//    }
//}

suspend fun getUnsolvedTask(client: CodeForcesClient, tasks: List<CodeForcesProblem>, users: List<String>): String{
    val solved = users.map {
        client.getUserSubmissions(it).result ?: return "ERROR: $it: not found"
    }.map {
        getSolvedTasks(it)
    }.fold(setOf<CodeForcesProblem>()){ acc, it->
        acc.union(it)
    }
    val unsolvedProblem = tasks.filter { it.contestId != null && it.index != null}.toSet().minus(solved).random()
    return "https://codeforces.com/contest/${unsolvedProblem.contestId}/problem/${unsolvedProblem.index}"
}

@OptIn(PrivilegedIntent::class)
suspend fun main(){
    val client = CodeForcesClient()

    println("Loading list of problems")
    val problems = client.getProblems()
    println("Got ${problems.size} problems")



    val kord = Kord("OTgyNjgyNTAxNTg1NzE5MzA3.G-3D8-.RgJCIwygsy1YhIAS_-mjgep_4Z3vJ22XJjGLls")

    kord.on<MessageCreateEvent> {
        if (message.author?.isBot != false) return@on
        if (message.content.trim() == "")return@on
        val users = message.content.trim().split(",")
        val response = getUnsolvedTask(client, problems, users)
        message.reply{
            this.content = response
        }
    }

    kord.login{
        intents += Intent.MessageContent
    }
}


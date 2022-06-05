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

@Serializable
enum class CodeForcesStatus(val status: String){
    OK("OK"),
    FAILED("FAILED")
}

@Serializable
data class CodeForcesResponse<T>(val status: CodeForcesStatus, val comment: String?, val result: T?)

@Serializable
data class CodeForcesUser(
    val handle: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
)

@Serializable
data class CodeForcesProblem(val contestId: Int?, val index: String?)

@Serializable
data class CodeForcesSubmission(val id: Int, val verdict: String?, val problem: CodeForcesProblem)

@Serializable
data class CodeForcesContest(val id: Int)

@Serializable
data class CodeForcesProblemSet(val problems: List<CodeForcesProblem>)

class CodeForcesClient{
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(CIO){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys=true
                explicitNulls = false
            })
        }
        install(DefaultRequest){
            url("https://codeforces.com/api/")
        }
    }

    suspend fun getUser(handle: String): CodeForcesResponse<List<CodeForcesUser>> {
        val response = client.get("user.info"){
            url.parameters.append("handles", handle)
        }.body<CodeForcesResponse<List<CodeForcesUser>>>()
        return response
    }

    suspend fun getUserSubmissions(handle: String): CodeForcesResponse<List<CodeForcesSubmission>>{
        val response = client.get("user.status"){
            url.parameters.append("handle", handle)
        }.body<CodeForcesResponse<List<CodeForcesSubmission>>>()

        return response
    }

    suspend fun userInfoToString(handle: String): String{
        val user = getUser(handle)
        val submissions = getUserSubmissions(handle)
        if(user.status == CodeForcesStatus.FAILED) return "$handle - ERROR: ${user.comment}"
        if(submissions.status == CodeForcesStatus.FAILED) return "$handle - ERROR: ${submissions.comment}"
        val userObject = user.result!![0]
        var response = "$handle\n"
        if(userObject.firstName != null || userObject.lastName != null){
            response += "Name: ${userObject.firstName?:""} ${userObject.lastName?:""}\n"
        }
        if(userObject.email != null){
            response += "Email: ${userObject.email}\n"
        }

        val uniqueSolvedTasks = mutableSetOf<CodeForcesProblem>()
        val submissionsObject = submissions.result!!

        val totalSubmissions = submissionsObject.size
        var successfulSubmissions = 0

//        val test = mutableMapOf<String, Int>()
        for(submission in submissionsObject){
//            if(submission.verdict != null){
//                test[submission.verdict] = (test[submission.verdict]?:0)+1
//            }
            if(submission.verdict == "OK"){
                successfulSubmissions += 1
                uniqueSolvedTasks.add(submission.problem)
            }
        }
//        test.forEach{
//            println("${it.key} ${it.value}")
//        }
        val solvedTasks = uniqueSolvedTasks.size

        response += "Total submissions: $totalSubmissions\n"
        response += "Successful submissions: $successfulSubmissions\n"
        response += "Solved tasks: $solvedTasks\n"
        return response
    }

//    var gymContests = listOf<CodeForcesContest>()

    suspend fun loadListOfContests(): List<CodeForcesContest>{
        val result = client.get("contest.list") {
            url.parameters.append("gym", true.toString())
        }.body<CodeForcesResponse<List<CodeForcesContest>>>()
        return result.result!!
    }

    suspend fun getProblems(): List<CodeForcesProblem>{
        val result = client.get("problemset.problems"){
        }.body<CodeForcesResponse<CodeForcesProblemSet>>()
        return result.result!!.problems
    }
}
package ru.leadpogrommer.vk22.discord

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.coroutines.runBlocking

enum class Tasks(val action: String){
    DISCORD("discord"),
    CLI("cli")
}

fun main(args: Array<String>){
    val parser = ArgParser("sportprog")
    val token by parser.option(ArgType.String)
    val what by parser.argument(ArgType.Choice<Tasks>())
    parser.parse(args)
    runBlocking {
        when(what){
            Tasks.DISCORD -> mainDiscord(token?:"")
            Tasks.CLI -> mainCli()
        }
    }

}
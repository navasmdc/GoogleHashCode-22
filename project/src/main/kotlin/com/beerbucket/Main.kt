package com.beerbucket

import java.io.File

    fun main(args: Array<String>) {
        File("input").listFiles().sortedBy { it.name }.forEachIndexed { index, file ->
            val input = file.readText()
            val output = execute(input)
            printOutput(output, index)
        }
    }

    fun execute(input: String) : String {
        // PUT YOUR FUCKING CODE HERE!!
        return input
    }

    fun printOutput(output: String, index: Int) = File("output/outputFile$index").writeText(output)

fun String.splitPro(string: String) = split(string).toMutableList().also { it.removeIf { s -> s.isEmpty() } }


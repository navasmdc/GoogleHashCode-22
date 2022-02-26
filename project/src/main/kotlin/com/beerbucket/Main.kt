package com.beerbucket

import java.io.File

var line = 0

val skills = mutableMapOf<String, MutableList<Dev>>()
val projects = mutableListOf<Project>()
val projectsDone = mutableListOf<Project>()

fun main(args: Array<String>) {
    File("input").listFiles().sortedBy { it.name }.forEachIndexed { index, file ->
        val input = file.readLines()
        line = 0
        val output = execute(input)
        printOutput(output, index)
    }
}

fun execute(lines: List<String>): String {
    val (devs, nProjects) = lines[line].splitPro(" ").run {
        get(0).toInt() to get(1).toInt()
    }
    line++
    // READ DEVS
    while (line <= lines.size - 1) {
        (0 until devs).forEach {
            val (devName, devSkills) = lines[line].splitPro(" ").run {
                get(0) to get(1).toInt()
            }
            val dev = Dev(devName)
            line++
            (0 until devSkills).forEach {
                val skill = lines[line].splitPro(" ").run {
                    val skillName = get(0)
                    dev.skills[skillName] = getInt(1)
                    skillName
                }
                if (skills.containsKey(skill)) {
                    skills[skill]?.add(dev)
                } else {
                    skills[skill] = mutableListOf(dev)
                }
                line++
            }
        }
        // READ PROJECTS
        (0 until nProjects).forEach {
            val (project, skillNumber) = lines[line].splitPro(" ").run {
                Project(name = get(0), days = getInt(1), score = getInt(2), bestDay = getInt(3)) to getInt(4)
            }
            line++
            (0 until skillNumber).forEach {
                val (skill, skillLevel) = lines[line].splitPro(" ").run {
                    get(0) to getInt(1)
                }
                project.skills.add(skill to skillLevel)
                line++
            }
            projects.add(project)
        }
    }

    val plannedProjects = mutableListOf<Pair<Project, List<Pair<String, Dev>>>>()

    while (projects.isNotEmpty()) {
        val projectCandidates = mutableListOf<Pair<Project, List<Pair<String, Dev>>>>()
        projects.forEach { project ->
            val temporalAssignments = mutableListOf<Pair<String, Dev>>()
            // CHECK IF IS THERE ANY DEV
            project.skills.forEach { skillProject ->
                val skill = skillProject.first
                val skillLevel = skillProject.second
                println(skillProject.first)
                if (skills.containsKey(skill)) {
                    println("has skill")
                    val candidates = skills[skill]!!.filter { dev ->
                        val hassSkillLevel = dev.skills[skill]!! >= skillLevel
                        val dayOffset = dev.lastBusyDay + project.days - project.bestDay
                        val worth = (project.score - dayOffset) > 0
                        hassSkillLevel && worth && !temporalAssignments.none { it.second == dev }
                    }
                    val candidatesWithoutOtherSkill = candidates.filter { dev ->
                        dev.skills.keys.filterNot { it == skill }.none { skillName ->
                            project.skills.any { it.first == skillName }
                        }
                    }
                    println(candidatesWithoutOtherSkill.joinToString { it.name })
                    val candidate = candidatesWithoutOtherSkill.minBy { it.skills.size } ?: candidates.minBy { it.skills.size }
                    candidate?.let {
                        temporalAssignments.add(skill to it)
                    }
                } else return@forEach
            }
            if (temporalAssignments.isNotEmpty() && project.skills.map { it.first }.containsAll(temporalAssignments.map { it.first })) {
                projectCandidates.add(project to temporalAssignments)
            }
        }
        if(projectCandidates.isEmpty()) break

        println("project Candidates:")
        println(projectCandidates.joinToString { it.first.name })

        val selectedProject = projectCandidates.sortedWith(compareBy({ it.first.bestDay }, { it.first.days })).first()
        println("selected project devs: ")
        println(selectedProject.second.joinToString(separator = " ") { it.second.name })
        println(selectedProject.second)
        val startDate = selectedProject.second.maxBy { it.second.lastBusyDay }!!.second.lastBusyDay + 1
        val endDate = startDate + selectedProject.first.days

        selectedProject.second.forEach { assignedSkills ->
            assignedSkills.second.run {
                if (skills[assignedSkills.first] == selectedProject.first.skills.first { it.first == assignedSkills.first }.second) {
                    skills[assignedSkills.first] = skills[assignedSkills.first]!!.plus(1)
                }

                assignments.add(startDate to endDate)
            }
        }

        plannedProjects.add(selectedProject)
        projects.remove(selectedProject.first)
    }

    val output = StringBuffer()

    output.append("${plannedProjects.size}\n")

    output.append(plannedProjects.joinToString(separator = "\n") { "${it.first.name}\n${it.second.joinToString(separator = " ") { dev -> dev.second.name }}" })

    return output.toString()
}

fun printOutput(output: String, index: Int) = File("output/outputFile$index").writeText(output)

fun String.splitPro(string: String) = split(string).toMutableList().also { it.removeIf { s -> s.isEmpty() } }
fun List<String>.getInt(position: Int) = get(position).toInt()


package ru.spbstu.terrai.feature

import ru.spbstu.terrai.lab.Controller
import ru.spbstu.terrai.lab.Labyrinth

fun main(args: Array<String>) {
    var won = 0
    var lose = 0
    for (i in 1..1000) {
        val lab = Labyrinth.createFromFile("labyrinths/lab3.txt")
        val player = Professional()
        val controller = Controller(lab, player)
        val result = controller.makeMoves(1000)
        if (result.exitReached) {
            ++won
        } else {
            ++lose
        }
    }
    println("Won - $won times")
    println("Lose - $lose times")
}
package ru.spbstu.terrai.feature

import ru.spbstu.terrai.lab.Controller
import ru.spbstu.terrai.lab.Labyrinth

fun main(args: Array<String>) {
    val lab = Labyrinth.createFromFile("labyrinths/lab3.txt")
    val player = Professional()
    val controller = Controller(lab, player)
    val result = controller.makeMoves(1000)
    if (result.exitReached) {
        println("You won!")
    }
    else {
        println("You lose!")
    }
}
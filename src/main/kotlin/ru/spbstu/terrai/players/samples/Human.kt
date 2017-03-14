package ru.spbstu.terrai.players.samples

import ru.spbstu.terrai.core.*

class Human : AbstractPlayer() {
    override fun getNextMove(): Move {
        println("Enter w (NORTH), d (EAST), s (SOUTH), a (WEST)")
        val answer = readLine()
        val move = when (answer) {
            "w" -> WalkMove(Direction.NORTH)
            "d" -> WalkMove(Direction.EAST)
            "s" -> WalkMove(Direction.SOUTH)
            "a" -> WalkMove(Direction.WEST)
            else -> WaitMove
        }
        return move
    }

    override fun setMoveResult(result: MoveResult) {
        println(result.status)
    }
}

//fun main(args: Array<String>) {
//    val lab = Labyrinth.createFromFile("labyrinths/lab3.txt")
//    val player = Human()
//    val controller = Controller(lab, player)
//    val result = controller.makeMoves(1000)
//    if (result.exitReached) {
//        println("You won!")
//    }
//    else {
//        println("You lose!")
//    }
//}
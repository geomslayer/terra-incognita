package ru.spbstu.terrai.feature

import ru.spbstu.terrai.core.*
import java.util.*

val wideRand = Random(9012661)
fun getRandomDirection(): Direction {
    return Direction.values()[wideRand.nextInt(4)]
}

class Professional : Player {

    var width = 0
    var height = 0
    var strategy = Spiral()

    override fun setStartLocationAndSize(location: Location, width: Int, height: Int) {
        this.height = height
        this.width = width
    }

    override fun getNextMove(): Move {
        val dir = strategy.nextStep()
        return if (dir == null) WaitMove else WalkMove(dir)
    }

    override fun setMoveResult(result: MoveResult) {
        when (result.room) {
            is Wormhole -> {
                strategy = Spiral(isHole = true)
            }
            else -> {
                strategy.applyResult(result)
            }
        }
    }

}

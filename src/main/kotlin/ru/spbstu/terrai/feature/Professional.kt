package ru.spbstu.terrai.feature

import ru.spbstu.terrai.core.*
import java.util.*

class Professional : Player {
    private val rand = Random()

    private var strategy = getStrategy()

    override fun setStartLocationAndSize(location: Location, width: Int, height: Int) {

    }

    override fun getNextMove(): Move {
        val dir = strategy.nextStep()
        return if (dir == null) WaitMove else WalkMove(dir)
    }

    override fun setMoveResult(result: MoveResult) {
        when (result.room) {
            is Wormhole -> {
                strategy = getStrategy(Wormhole(0))
            }
            else -> {
                strategy.applyResult(result)
            }
        }
    }

    private fun getStrategy(startRoom: Room = Empty): Strategy {
        val mod = 4 - if (startRoom is Wormhole) 0 else 1
        return when (rand.nextInt(mod)) {
            0 -> Spiral(startRoom)
            1, 2 -> Chaotic(startRoom)
            else -> Return(startRoom)
        }
    }

}

package ru.spbstu.terrai.feature

import ru.spbstu.terrai.core.*
import java.util.*

class Professional : Player {
    val rand = Random()

    var width = 0
    var height = 0
    var strategy = getStrategy(false)

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
                strategy = getStrategy(true)
            }
            else -> {
                strategy.applyResult(result)
            }
        }
    }

    private fun getStrategy(isHole: Boolean): Strategy {
        val mod = 4 - if (isHole) 0 else 1
        return when(rand.nextInt(mod)) {
            0 -> Spiral(isHole = isHole)
            1, 2 -> Chaotic(isHole = isHole)
            else -> Return(isHole = isHole)
        }
    }

}

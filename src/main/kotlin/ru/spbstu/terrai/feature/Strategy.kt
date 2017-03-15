package ru.spbstu.terrai.feature

import ru.spbstu.terrai.core.*
import java.util.*
import kotlin.collections.HashMap

val wideRand = Random()
fun getRandomDirection(): Direction {
    return Direction.values()[wideRand.nextInt(4)]
}

abstract class Strategy(strategy: Strategy? = null, isHole: Boolean = false) {
    val rand = Random()
    val map: HashMap<Location, Room>

    var location: Location
    var newLocation: Location

    var lastRoom: Room? = null
    var lastDirection: Direction? = null

    var hasPlan = false
    var foundTreasure = false
    var steps = ArrayDeque<Direction>()

    var left = rand.nextBoolean()

    init {
        if (strategy == null) {
            map = HashMap<Location, Room>()
            location = Location(0, 0)
        } else {
            map = strategy.map
            location = strategy.location
        }
        map[location] = if (isHole) Wormhole(0) else Empty
        newLocation = location
    }

    fun nextStep(): Direction? {
        if (foundTreasure) {
            foundTreasure = false
            if (findPath(Exit)) {
                hasPlan = true
            }
        }

        if (hasPlan) {
            if (steps.isNotEmpty()) {
                lastDirection = steps.poll()
            } else {
                hasPlan = false
            }
        } else {
            lastDirection = nextDirection()
        }

        if (lastDirection == null && (findPath(null) || findPath(Wormhole(0)))) {
            lastDirection = steps.poll()
            hasPlan = true
        }

        if (lastDirection != null) {
            newLocation = lastDirection!! + location
        }

        return lastDirection
    }

    fun applyResult(res: MoveResult) {
        map[newLocation] = res.room
        lastRoom = res.room

        if (res.successful) {
            location = newLocation

            if (res.condition.hasTreasure) {
                foundTreasure = true
            }
        }
    }

    internal fun findPath(room: Room? = null): Boolean {
        val used = HashSet<Location>()
        val queue = ArrayDeque<Location>()
        val path = HashMap<Location, Direction?>()

        queue.offer(location)
        used.add(location)
        path[location] = null

        var target: Location? = null

        main@ while (queue.isNotEmpty()) {
            val cur = queue.poll()

            for (dir in Direction.values()) {
                val nxt = dir + cur

                if (used.contains(nxt) || (map.containsKey(nxt) && map[nxt] is Wall)) {
                    continue
                }

                used.add(nxt)
                path[nxt] = dir

                val nxtRoom = map[nxt]
                if (nxtRoom is Empty || nxtRoom is Exit || nxtRoom is Entrance || nxtRoom is WithContent) {
                    queue.offer(nxt)
                }

                if (room == map[nxt]) {
                    target = nxt
                    break@main
                }
            }
        }

        if (target == null) {
            return false
        }

        steps.clear()
        while (path[target] != null) {
            steps.offerFirst(path[target])
            target = path[target]!!.turnBack() + target!!
        }

        return true
    }

    internal fun turn(dir: Direction?, reverse: Boolean): Direction {
        return when (left xor reverse) {
            true -> dir?.turnLeft() ?: getRandomDirection()
            false -> dir?.turnRight() ?: getRandomDirection()
        }
    }

    abstract fun nextDirection(): Direction?

}

class Spiral(strategy: Strategy? = null, isHole: Boolean = false) : Strategy(strategy, isHole) {

    override fun nextDirection(): Direction? {
        var newDirection: Direction? = turn(lastDirection, false)

        var nxt: Location = newDirection!! + location
        var cnt = 0
        while (map[nxt] != null && cnt < 4) {
            newDirection = turn(newDirection, true)
            nxt = newDirection + location
            ++cnt
        }

        if (cnt == 4) {
            return null
        }

        return newDirection
    }

}

class Chaotic(strategy: Strategy? = null, isHole: Boolean = false) : Strategy(strategy, isHole) {

    override fun nextDirection(): Direction? {
        var newDirection = when (rand.nextInt(3)) {
            0 -> lastDirection?.turnLeft()
            1 -> lastDirection
            2 -> lastDirection?.turnRight()
            else -> null
        }
        if (newDirection == null) {
            newDirection = getRandomDirection()
        }

        left = rand.nextBoolean()
        var nxt = newDirection + location
        var cnt = 0
        while (map.containsKey(nxt) && cnt < 4) {
            newDirection = turn(newDirection, false)
            nxt = newDirection + location
            ++cnt
        }

        if (cnt == 4) {
            return null
        }

        return newDirection
    }

}

class Return(strategy: Strategy? = null, isHole: Boolean = false) : Strategy(strategy, isHole) {

    val start: Location = location

    override fun nextDirection(): Direction? {
        if (start == location) {
            var newDirection = if (lastDirection == null) {
                getRandomDirection()
            } else {
                lastDirection as Direction
            }

            var nxt = newDirection + location
            var cnt = 0
            while (map.containsKey(nxt) && cnt < 4) {
                newDirection = turn(newDirection, false)
                nxt = newDirection + location
                ++cnt
            }

            if (cnt == 4) {
                return null
            }

            return newDirection
        }

        return lastDirection!!.turnBack()
    }

}
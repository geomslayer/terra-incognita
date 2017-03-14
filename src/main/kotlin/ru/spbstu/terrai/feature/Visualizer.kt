package ru.spbstu.terrai.feature

import ru.spbstu.terrai.core.*
import ru.spbstu.terrai.lab.Labyrinth

fun Labyrinth.visualize(cur: Location) {
    for (y in -1..this.height) {
        for (x in -1..this.width) {
            if (cur == Location(x, y)) {
                print('*')
                continue
            }
            val ch = when (this[x, y]) {
                is Empty -> ' '
                is Wall -> '#'
                is Wormhole -> 'O'
                is Entrance -> 'S'
                is Exit -> 'E'
                is WithContent -> if (this[x, y].content != null) 'T' else ' '
                else -> '?'
            }
            print(ch)
        }
        println()
    }
}
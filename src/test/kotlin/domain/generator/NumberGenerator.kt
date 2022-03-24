package domain.generator

import kotlin.random.Random

class NumberGenerator {
    companion object {
        fun generateRandomList(numberOfItems: Int): List<Int> {
            return List(numberOfItems) { Random.nextInt(100000000, 999999999) }
        }
    }
}
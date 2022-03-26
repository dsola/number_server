package domain.generator

import kotlin.random.Random

class NumberGenerator {
    companion object {
        fun generateRandomList(numberOfItems: Int): List<Int> {
            return List(numberOfItems) { generateRandomNumber() }
        }

        fun generateRandomNumber(): Int {
            return Random.nextInt(100000000, 999999999)
        }
    }
}
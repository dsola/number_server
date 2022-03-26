package adapter.`in`.writer

import adapter.out.MemoryNumberHistoryRepository
import java.util.LinkedList
import java.util.Queue

class NumberQueueWriter(numberHistoryRepository: MemoryNumberHistoryRepository) {
    private val queue: Queue<Int> = LinkedList()

    fun writeNewNumber(number: Int) {
        queue.add(number)
    }

    fun startWorker() {
        while (true) {
            waitForNumber()
            val newNumber = queue.remove()

        }
    }

    fun waitForNumber() {
        while (queue.isEmpty()) {}
    }
}

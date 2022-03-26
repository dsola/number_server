package adapter.`in`.writer

import java.util.LinkedList
import java.util.Queue

class NumberQueueWriter(private val writeNumber: WriteNumber) {
    private val queue: Queue<Int> = LinkedList()

    fun writeNewNumber(number: Int) {
        queue.add(number)
    }

    fun startWorker() {
        while (true) {
            waitForNumber()
            writeNumber.write(queue.remove())
        }
    }

    fun waitForNumber() {
        while (queue.isEmpty()) {}
    }
}

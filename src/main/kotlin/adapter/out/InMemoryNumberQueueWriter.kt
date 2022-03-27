package adapter.out

import adapter.`in`.writer.WriteNumber
import domain.contract.NumberQueueWriter
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.LinkedList
import java.util.Queue

@DelicateCoroutinesApi
class InMemoryNumberQueueWriter(private val writeNumber: WriteNumber) : NumberQueueWriter {
    private val queue: Queue<Int> = LinkedList()
    private val workers: MutableList<Job> = mutableListOf()
    private val mutex = Mutex()

    override fun writeNewNumber(number: Int) {
        queue.add(number)
    }

    override fun start(numberOfWorkers: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            for (i in 1..numberOfWorkers) {
                workers.add(launch { startWorker() })
            }
            for (job in workers) {
                job.join()
            }
        }
    }

    override fun stop() {
        GlobalScope.launch(Dispatchers.IO) {
            for (job in workers) {
                job.cancel()
            }
        }
    }

    private fun startWorker() = runBlocking {
        while (true) {
            waitForNumber()
            var lastNumber: Int
            mutex.withLock {
                lastNumber = queue.remove()
            }
            lastNumber.let { writeNumber.write(lastNumber) }
        }
    }

    private fun waitForNumber() {
        while (queue.isEmpty()) {
        }
    }
}

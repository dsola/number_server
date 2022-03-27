package adapter.out

import adapter.`in`.writer.WriteNumber
import domain.contract.NumberQueueWriter
import kotlinx.coroutines.*
import java.util.LinkedList
import java.util.Queue

@DelicateCoroutinesApi
class InMemoryNumberQueueWriter(private val writeNumber: WriteNumber) : NumberQueueWriter {
    private val queue: Queue<Int> = LinkedList()

    override fun writeNewNumber(number: Int) {
        queue.add(number)
    }

    override fun start(numberOfWorkers: Int) {
        val workers = mutableListOf<Job>()
        GlobalScope.launch(Dispatchers.IO) {
            for (i in 1..numberOfWorkers) {
                workers.add(launch { startWorker() })
            }
            for (job in workers) {
                job.join()
            }
        }
    }

    private suspend fun startWorker() = runBlocking {
        while (true) {
            waitForNumber()
            writeNumber.write(queue.remove())
        }
    }

    private fun waitForNumber() {
        while (queue.isEmpty()) {}
    }
}

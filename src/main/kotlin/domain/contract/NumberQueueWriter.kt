package domain.contract

interface NumberQueueWriter {
    fun writeNewNumber(number: Int)
    fun start(numberOfWorkers: Int)
}
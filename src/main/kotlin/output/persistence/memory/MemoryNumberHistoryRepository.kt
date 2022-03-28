package output.persistence.memory

import output.persistence.NumberHistoryRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MemoryNumberHistoryRepository : NumberHistoryRepository {
    private val numbersMap: MutableMap<Int, Boolean> = HashMap()
    private var uniqueNumbers: Int = 0
    private var duplicateNumbers: Int = 0
    private val mutex = Mutex()

    override fun countUniqueNumbers(): Int {
        return uniqueNumbers
    }

    override fun countDuplicateNumbers(): Int {
        return duplicateNumbers
    }

    override suspend fun persistNumber(number: Int) {
        if (!isNumberAlreadyPersisted(number)) {
            mutex.withLock {
                numbersMap[number] = false
                ++uniqueNumbers
            }

            return
        }
        if (!wasAccessedBefore(number)) {
            mutex.withLock {
                numbersMap[number] = true
                ++duplicateNumbers
                --uniqueNumbers
            }
        }
    }

    override fun isNumberAlreadyPersisted(number: Int): Boolean {
        return numbersMap.containsKey(number)
    }

    private fun wasAccessedBefore(number: Int): Boolean {
        return numbersMap.containsKey(number) && numbersMap[number] == true
    }
}

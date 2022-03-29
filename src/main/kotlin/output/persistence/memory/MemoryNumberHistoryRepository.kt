package output.persistence.memory

import output.persistence.NumberHistoryRepository

class MemoryNumberHistoryRepository : NumberHistoryRepository {
    private val duplicatedNumbers: MutableMap<Int, Boolean> = HashMap()
    private var uniqueNumbersCounter: Int = 0
    private var duplicateNumbersCounter: Int = 0

    override fun countUniqueNumbers(): Int {
        return uniqueNumbersCounter
    }

    override fun countDuplicateNumbers(): Int {
        return duplicateNumbersCounter
    }

    override suspend fun persistNumber(number: Int) {
        if (!isNumberAlreadyPersisted(number)) {
            duplicatedNumbers[number] = false
            ++uniqueNumbersCounter
            return
        }
        if (!wasAccessedBefore(number)) {
            duplicatedNumbers[number] = true
            ++duplicateNumbersCounter
            --uniqueNumbersCounter
        }
    }

    override fun isNumberAlreadyPersisted(number: Int): Boolean {
        return duplicatedNumbers.containsKey(number)
    }

    private fun wasAccessedBefore(number: Int): Boolean {
        return duplicatedNumbers.containsKey(number) && duplicatedNumbers[number] == true
    }
}

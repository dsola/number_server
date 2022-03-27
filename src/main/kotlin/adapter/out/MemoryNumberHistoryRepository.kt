package adapter.out

import domain.contract.NumberHistoryRepository

class MemoryNumberHistoryRepository : NumberHistoryRepository {
    private val numbersMap: MutableMap<Int, Boolean> = HashMap()
    private var uniqueNumbers: Int = 0
    private var duplicateNumbers: Int = 0

    override fun countUniqueNumbers(): Int {
        return uniqueNumbers
    }

    override fun countDuplicateNumbers(): Int {
        return duplicateNumbers
    }

    override fun persistNumber(number: Int) {
        if (!isNumberAlreadyPersisted(number)) {
            numbersMap[number] = false
            ++uniqueNumbers
            return
        }
        if (!wasAccessedBefore(number)) {
            numbersMap[number] = true
            ++duplicateNumbers
            --uniqueNumbers
        }
    }

    override fun isNumberAlreadyPersisted(number: Int): Boolean {
        return numbersMap.containsKey(number)
    }

    private fun wasAccessedBefore(number: Int): Boolean {
        return numbersMap.containsKey(number) && numbersMap[number] == true
    }
}

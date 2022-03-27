package implementation

import domain.contract.NumberHistoryRepository

class TestNumberHistoryRepository(
    private val uniqueNumbers: List<Int>,
    private val duplicateNumbers: List<Int>
) : NumberHistoryRepository {
    override fun countUniqueNumbers(): Int {
        return uniqueNumbers.count()
    }

    override fun countDuplicateNumbers(): Int {
        return duplicateNumbers.count()
    }

    override fun persistUniqueNumber(number: Int) {}
    override fun persistDuplicateNumber(number: Int) {}

    override fun isNumberAlreadyPersisted(number: Int): Boolean {
        return true
    }
}

package adapter.out

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
}

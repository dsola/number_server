package adapter.out

import domain.contract.NumberHistoryRepository

class MemoryNumberHistoryRepository : NumberHistoryRepository {
    override fun countUniqueNumbers(): Int {
        TODO("Not yet implemented")
    }

    override fun countDuplicateNumbers(): Int {
        TODO("Not yet implemented")
    }
}